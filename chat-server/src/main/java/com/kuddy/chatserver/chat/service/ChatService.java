package com.kuddy.chatserver.chat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.kuddy.common.chat.exception.NotChatRoomOwnerException;
import com.kuddy.common.chat.exception.NotMatchLoginMemberEmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.chatserver.chat.dto.request.ChatReqDto;
import com.kuddy.chatserver.chat.dto.response.ChatHistoryResDto;
import com.kuddy.chatserver.chat.dto.response.ChatResDto;
import com.kuddy.chatserver.chat.dto.response.ChatRoomListResDto;
import com.kuddy.common.chat.exception.ChatNotFoundException;
import com.kuddy.common.chat.exception.RoomNotFoundException;
import com.kuddy.common.chat.repository.MongoChatRepository;
import com.kuddy.common.chat.repository.RoomRepository;
import com.kuddy.common.jwt.JwtProvider;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.exception.NotAuthorException;
import com.kuddy.common.member.repository.MemberRepository;

import com.kuddy.common.util.ConstantUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
	private final MongoTemplate mongoTemplate;
	private final MongoChatRepository mongoChatRepository;
	private final RoomRepository roomRepository;
	private final ChatQueryService chatQueryService;

	private final MessageSender sender;
	private final MemberRepository memberRepository;
	private final MeetupService meetupService;
	private final ChatRoomService chatRoomService;
	//private final NotificationService notificationService;
	private final JwtProvider jwtProvider;
	private static final String MEETUP_TYPE = "MEETUP";

	public Long makeChatRoom(Member member, ChatReqDto requestDto) {
		Member joinMember = memberRepository.findByNickname(requestDto.getCreateMemberNickname())
			.orElseThrow(MemberNotFoundException::new);

		Room room = Room.builder()
			.createMember(member)
			.joinMember(joinMember)
			.build();

		//todo: 이미 존재하는 방 예외 추가
		Room savedRoom = roomRepository.save(room);

		return savedRoom.getId();
	}

	@Transactional(readOnly = true)
	public List<ChatRoomListResDto> getChatList(Member member) {
		List<ChatRoomListResDto> chatRoomList = chatQueryService.getChattingList(member.getId());

		chatRoomList
			.forEach(chatRoomDto -> {
				// 채팅방별로 읽지 않은 메시지 개수를 셋팅
				long unReadCount = countUnReadMessages(chatRoomDto.getChatRoomId(), member.getNickname());
				chatRoomDto.setUnReadCount(unReadCount);

				// 채팅방별로 마지막 채팅내용과 시간을 셋팅
				Page<Chatting> chatting =
					mongoChatRepository.findByRoomIdOrderBySendTimeDesc(chatRoomDto.getChatRoomId(),
						PageRequest.of(0, 1));
				if (chatting.hasContent()) {
					Chatting chat = chatting.getContent().get(0);
					ChatRoomListResDto.LatestMessage latestMessage = ChatRoomListResDto.LatestMessage.builder()
						.context(chat.getContent())
						.sendTime(chat.getSendTime())
						.build();
					chatRoomDto.setLatestMessage(latestMessage);
				}
			});

		return chatRoomList;
	}

	@Transactional(readOnly = true)
	public ChatHistoryResDto getChattingList(Long chatRoomId, Member member) {
		List<ChatResDto> chattingList = mongoChatRepository.findByRoomId(chatRoomId)
			.stream()
			.map(chat -> new ChatResDto(chat, member.getNickname()))
			.collect(Collectors.toList());

		return ChatHistoryResDto.builder()
			.chatList(chattingList)
			.email(member.getEmail())
			.build();
	}

	@Transactional(readOnly = true)
	public void sendMessage(Message message, String authorization) {
		String email = jwtProvider.tokenToEmail(authorization);
		Member findMember = findByEmail(email);
		// 채팅방에 모든 유저가 참여중인지 확인한다.
		boolean isConnectedAll = chatRoomService.isAllConnected(message.getRoomId());
		// 1:1 채팅이므로 2명 접속시 readCount 0, 한명 접속시 1
		Integer readCount = isConnectedAll ? 0 : 1;
		// message 객체에 보낸시간, 보낸사람 memberNo, 닉네임을 셋팅해준다.
		message.setSendTimeAndSender(LocalDateTime.now(), findMember.getNickname(),
			readCount);
		// 메시지를 전송한다.
		sender.send(ConstantUtil.KAFKA_TOPIC, message);
	}

	public Message sendNotificationAndSaveMessage(Message message) {
		// 메시지 저장과 알람 발송을 위해 메시지를 보낸 회원을 조회
		Member findMember = findByEmail(message.getSenderEmail());
		Member receiveMember = chatQueryService.getReceiverNumber(message.getRoomId(), message.getSenderName());
		// 상대방이 읽지 않은 경우에만 알림 전송
		if (message.getReadCount() == 1) {
			// 알람 전송을 위해 메시지를 받는 사람을 조회한다.
			// TODO : 알림을 전송한다.

		}

		// 보낸 사람일 경우에만 메시지를 저장 -> 중복 저장 방지
		if (message.getSenderEmail().equals(findMember.getEmail())) {
			// Message 객체를 채팅 엔티티로 변환한다.
			Chatting chatting = message.convertEntity();
			// 채팅 내용을 저장한다.
			Chatting savedChat = mongoChatRepository.save(chatting);
			// 저장된 고유 ID를 반환한다.
			message.setId(savedChat.getId());
			if(message.getContentType().equals(MEETUP_TYPE)){
				meetupService.create(message, findMember, receiveMember);
			}

		}
		return message;
	}
	public void updateMessage(Message message, String authorization) throws IOException {
		String email = jwtProvider.tokenToEmail(authorization);
		checkvalidateMember(email, message.getRoomId());
		if(message.getContentType().equals(MEETUP_TYPE)){
			Chatting chatting = mongoChatRepository.findById(message.getId()).orElseThrow(ChatNotFoundException::new);
			chatting.setAppointmentTime(message.getAppointmentTime());
			chatting.setSpotContentId(message.getSpotContentId());
			chatting.setSpotName(message.getSpotName());
			chatting.setMeetStatus(message.getMeetStatus());
			chatting.setPrice(message.getPrice());
			chatting.setReadCount(message.getReadCount());
			meetupService.update(message);
			sender.send(ConstantUtil.KAFKA_TOPIC, message);
		}
	}
	private void checkvalidateMember(String email, Long roomId){
		Room room = findByRoomId(roomId);
		String senderEmail = room.getCreateMember().getEmail();
		String joinEmail = room.getJoinMember().getEmail();
		if(!email.equals(senderEmail) && !email.equals(joinEmail)){
			throw new NotAuthorException();
		}
	}


	public void updateMessage(String email, Long chatRoomNo) {
		Message message = Message.builder()
			.contentType("NOTI")
			.roomId(chatRoomNo)
			.content(email + " 님이 돌아오셨습니다.")
			.build();

		sender.send(ConstantUtil.KAFKA_TOPIC, message);
	}

	// 읽지 않은 메시지 채팅장 입장시 읽음 처리
	public void updateCountAllZero(Long chatRoomId, String email) {
		Member findMember = findByEmail(email);
		Update update = new Update().set("readCount", 0);
		Query query = new Query(Criteria.where("roomId").is(chatRoomId)
			.and("senderName").ne(findMember.getNickname()));
		mongoTemplate.updateMulti(query, update, Chatting.class);
	}

	// 읽지 않은 메시지 카운트
	long countUnReadMessages(Long roomId, String senderNickname) {
		Query query = new Query(Criteria.where("roomId").is(roomId)
			.and("readCount").is(1)
			.and("senderName").ne(senderNickname));

		return mongoTemplate.count(query, Chatting.class);
	}


	@Transactional(readOnly = true)
	public Room findByRoomId(Long roomId){
		return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
	}

	public String checkRoomIdOwnerValidation(Member member, Long roomId) {
		boolean isValid = roomRepository.existsByRoomIdAndAnyMember(roomId, member);
		if(isValid){
			return member.getEmail();
		}
		else{
			throw new NotChatRoomOwnerException();
		}
	}

	public void checkEmailValidation(String loginEmail, String email) {
		if(!loginEmail.equals(email)){
			throw new NotMatchLoginMemberEmailException();
		}
	}

	@Transactional(readOnly = true)
	public Room findByMembers(Member member, String email) {
		Member targetMember = findByEmail(email);
		return roomRepository.findRoomByMembers(member, targetMember);
	}

	@Transactional(readOnly = true)
	public Member findByEmail(String email){
		return memberRepository.findByEmail(email)
				.orElseThrow(MemberNotFoundException::new);
	}
}
