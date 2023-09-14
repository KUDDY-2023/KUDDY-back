package com.kuddy.chatserver.chat.controller;

import com.kuddy.chatserver.chat.service.ChatService;
import com.kuddy.common.chat.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/chat/v1")
@RequiredArgsConstructor
public class ChattingController {
    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {
        chatService.sendMessage(message, accessToken);
    }

    @MessageMapping("/updateMessage")
    public void updateMessage(@Valid Message updatedMessage, @Header("Authorization") final String accessToken) throws IOException {
        // 데이터베이스 상태 업데이트 또는 서비스 로직 실행
        chatService.updateMessage(updatedMessage, accessToken);
    }
}
