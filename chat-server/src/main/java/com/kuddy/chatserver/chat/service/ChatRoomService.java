package com.kuddy.chatserver.chat.service;

import com.kuddy.chatserver.chat.dto.request.ChatReqDto;
import com.kuddy.chatserver.chat.dto.response.ChatRoomListResDto;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.chat.domain.mongo.Chatting;
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
}
