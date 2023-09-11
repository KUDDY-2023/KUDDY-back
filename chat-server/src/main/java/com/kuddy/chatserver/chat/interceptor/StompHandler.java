package com.kuddy.chatserver.chat.interceptor;

import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.kuddy.chatserver.chat.service.ChatRoomService;
import com.kuddy.chatserver.chat.service.ChatService;
import com.kuddy.common.jwt.JwtProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {
	private final JwtProvider jwtProvider;
	private final ChatRoomService chatRoomService;
	private final ChatService chatService;
	private static final String BEARER_PREFIX = "Bearer ";
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
			// 헤더 토큰 얻기
			log.info(String.valueOf(headerAccessor));
			String accessToken = getAccessToken(headerAccessor);
			log.info(accessToken);
			String email = verifyAccessToken(accessToken);
			log.info("StompAccessor = {}", headerAccessor);
			handleMessage(headerAccessor.getCommand(), headerAccessor, email);
		}
		return message;
	}

	public void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String email) {
		switch (stompCommand) {

			case CONNECT:
				connectToChatRoom(accessor, email);
				break;
			case SUBSCRIBE:
			case SEND:
				jwtProvider.validateToken(getAccessToken(accessor));
				break;
		}
	}

	private String getAccessToken(StompHeaderAccessor headerAccessor) {
		String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

		if(authorizationHeader == null || authorizationHeader.equals("null")){
			throw new MessageDeliveryException("메세지 예외");
		}
		String token = authorizationHeader;
		if(authorizationHeader.startsWith(BEARER_PREFIX)){
			token = authorizationHeader.substring(BEARER_PREFIX.length());
		}
		else if(authorizationHeader.startsWith("[Bearer ")){
			token = authorizationHeader.substring(BEARER_PREFIX.length()+1);
		}
		if (token.endsWith("]")) {
			token = token.substring(0, token.length() - 1);
		}
		return token;
	}

	public void connectToChatRoom(StompHeaderAccessor accessor, String email) {
		// 채팅방 번호를 가져온다.
		Long chatRoomNo = getChatRoomNo(accessor);

		// 채팅방 입장 처리 -> Redis에 입장 내역 저장
		chatRoomService.connectChatRoom(chatRoomNo, email);
		// 읽지 않은 채팅을 전부 읽음 처리
		chatService.updateCountAllZero(chatRoomNo, email);
		// 현재 채팅방에 접속중인 인원이 있는지 확인한다.
		boolean isConnected = chatRoomService.isConnected(chatRoomNo);

		if (isConnected) {
			chatService.updateMessage(email, chatRoomNo);
		}
	}

	private String verifyAccessToken(String token) {

		if (!jwtProvider.validateToken(token)) {
			throw new IllegalStateException("토큰이 만료되었습니다.");
		}
		Claims claims = jwtProvider.getClaims(token);
		String email = claims.getSubject();

		return email;
	}

	private Long getChatRoomNo(StompHeaderAccessor accessor) {
		return
			Long.valueOf(
				Objects.requireNonNull(
					accessor.getFirstNativeHeader("roomId")
				));
	}
}
