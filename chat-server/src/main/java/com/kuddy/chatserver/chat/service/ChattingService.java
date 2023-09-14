package com.kuddy.chatserver.chat.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kuddy.chatserver.chat.dto.response.ReceiverInfoResDto;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.chatserver.chat.dto.response.ChatHistoryResDto;
import com.kuddy.chatserver.chat.dto.response.ChatResDto;
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
public class ChattingService {
	private final MongoTemplate mongoTemplate;
	private final MongoChatRepository mongoChatRepository;
	private final RoomRepository roomRepository;
	private final ChatQueryService chatQueryService;

	private final MessageSender sender;
	private final MemberRepository memberRepository;
	private final MeetupService meetupService;
	private final ChatRoomConnectInfoService chatRoomConnectInfoService;
	//private final NotificationService notificationService;
	private final JwtProvider jwtProvider;
	private static final String MEETUP_TYPE = "MEETUP";


	@Transactional(readOnly = true)
	public ChatHistoryResDto getChattingList(Long chatRoomId, Member member) {
		List<ChatResDto> chattingList = getChatResDtos(chatRoomId, member.getNickname());
		Room room = findByRoomId(chatRoomId);
		Member receiver = findReceiver(room, member);

		return ChatHistoryResDto.builder()
				.chatList(chattingList)
				.receiverInfo(ReceiverInfoResDto.of(receiver))
				.build();
	}
	private List<ChatResDto> getChatResDtos(Long chatRoomId, String nickname) {
		return mongoChatRepository.findByRoomId(chatRoomId)
				.stream()
				.map(chat -> new ChatResDto(chat, nickname))
				.collect(Collectors.toList());
	}
	private Member findReceiver(Room room, Member member) {
		return Optional.of(room)
				.map(r -> r.getCreateMember().getId().equals(member.getId()) ? r.getJoinMember() : r.getCreateMember())
				.orElse(null);  // Handle null case appropriately
	}

	@Transactional(readOnly = true)
	public void sendMessage(Message message, String authorization) {
		String email = jwtProvider.tokenToEmail(authorization);
		Member findMember = findByEmail(email);
		// 채팅방에 모든 유저가 참여중인지 확인한다.
		boolean isConnectedAll = chatRoomConnectInfoService.isAllConnected(message.getRoomId());
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




	@Transactional(readOnly = true)
	public Room findByRoomId(Long roomId){
		return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public Member findByEmail(String email){
		return memberRepository.findByEmail(email)
				.orElseThrow(MemberNotFoundException::new);
	}
}
