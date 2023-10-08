package com.kuddy.common.chat.service;

import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.common.chat.repository.MongoChatRepository;
import com.kuddy.common.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChattingUpdateService {
    private final MongoChatRepository mongoChatRepository;
    public void updateSenderNameInChatting(Member member) {
        List<Chatting> chats = mongoChatRepository.findBySenderId(member.getId());

        for (Chatting chat : chats) {
            chat.updateSenderName(member.getNickname());
            mongoChatRepository.save(chat);
        }
    }
}
