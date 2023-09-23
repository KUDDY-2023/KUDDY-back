package com.kuddy.chatserver.chat.service;

import com.kuddy.chatserver.chat.dto.request.ChatReqDto;
import com.kuddy.chatserver.chat.dto.response.ChatRoomListResDto;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.common.chat.exception.ChatRoomAlreadyExistsException;
import com.kuddy.common.chat.exception.NoRoomExistsException;
import com.kuddy.common.chat.exception.NotChatRoomOwnerException;
import com.kuddy.common.chat.exception.NotMatchLoginMemberEmailException;
import com.kuddy.common.chat.exception.RoomNotFoundException;
import com.kuddy.common.chat.repository.MongoChatRepository;
import com.kuddy.common.chat.repository.RoomRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final MongoTemplate mongoTemplate;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final MongoChatRepository mongoChatRepository;
    private final ChatQueryService chatQueryService;


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
    public long getTotalUnreadMessages(Member member) {
        // 1. 회원이 참여하고 있는 모든 채팅방의 ID 목록을 가져온다.
        List<Long> chatRoomIdList = getChattingRoomIds(member.getId());

        // 2. 각 채팅방에서 읽지 않은 메시지의 수를 계산한다.
        long totalUnreadCount = 0;
        for (Long chatRoomId : chatRoomIdList) {
            long unReadCount = countUnReadMessages(chatRoomId, member.getNickname());
            totalUnreadCount += unReadCount;
        }

        // 3. 총 읽지 않은 메시지의 수를 반환한다.
        return totalUnreadCount;
    }


    // 읽지 않은 메시지 카운트
    long countUnReadMessages(Long roomId, String senderNickname) {
        Query query = new Query(Criteria.where("roomId").is(roomId)
                .and("readCount").is(1)
                .and("senderName").ne(senderNickname));

        return mongoTemplate.count(query, Chatting.class);
    }

    // 읽지 않은 메시지 채팅장 입장시 읽음 처리
    public void updateCountAllZero(Long chatRoomId, String email) {
        Member findMember = findByEmail(email);
        Update update = new Update().set("readCount", 0);
        Query query = new Query(Criteria.where("roomId").is(chatRoomId)
                .and("senderName").ne(findMember.getNickname()));
        mongoTemplate.updateMulti(query, update, Chatting.class);
    }
    public Long makeChatRoom(Member createMember, ChatReqDto requestDto) {
        Member joinMember = memberRepository.findByNickname(requestDto.getCreateMemberNickname())
                .orElseThrow(MemberNotFoundException::new);
        Optional<Room> existingRoomOpt = Optional.ofNullable(findByMembers(createMember, joinMember.getEmail()));

        if (existingRoomOpt.isPresent()) {
            throw new ChatRoomAlreadyExistsException("roomId: " + existingRoomOpt.get().getId());
        } else {
            Room newRoom = Room.builder()
                .createMember(createMember)
                .joinMember(joinMember)
                .build();
            return roomRepository.save(newRoom).getId();
        }


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
    public Room findByRoomId(Long roomId){
        return roomRepository.findById(roomId).orElseThrow(RoomNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Room findByMembers(Member member, String email) {
        Member targetMember = findByEmail(email);
        return roomRepository.findRoomByMembers(member, targetMember);
    }
    @Transactional(readOnly = true)
    public List<Long> getChattingRoomIds(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        List<Room> rooms = roomRepository.findByCreateMemberOrJoinMember(member, member);
        if (rooms == null || rooms.isEmpty()) {
            throw new NoRoomExistsException();
        }

        return rooms.stream()
            .map(Room::getId)
            .collect(Collectors.toList());

    }
}
