package com.kuddy.chatserver.chat.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.chatserver.chat.dto.request.ChatReqDto;
import com.kuddy.chatserver.chat.dto.response.ChatHistoryResDto;
import com.kuddy.chatserver.chat.dto.response.ChatRoomListResDto;
import com.kuddy.chatserver.chat.dto.response.ChatRoomResDto;
import com.kuddy.chatserver.chat.service.ChatRoomService;
import com.kuddy.chatserver.chat.service.ChatService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat/v1")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private static final String CHAT_ROOM_DISCONNECTED= "성공적으로 접속 해제했습니다.";

	@PostMapping("/chatrooms")
	public ResponseEntity<StatusResponse> createChatRoom(@RequestBody @Valid final ChatReqDto requestDto, @AuthUser Member member) {

		// 채팅방을 만들어준다.
		Long roomId = chatService.makeChatRoom(member, requestDto);
		Room room = chatService.findByRoomId(roomId);
		ChatRoomResDto response = new ChatRoomResDto(room);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(roomId)
			.toUri();

		return ResponseEntity.created(location)
			.body(StatusResponse.builder()
				.status(StatusEnum.CREATED.getStatusCode())
				.message(StatusEnum.CREATED.getCode())
				.data(response)
				.build());
	}

	// 채팅내역 조회 AuthUSER
	@GetMapping("/chatrooms/{roomId}")
	public ResponseEntity<StatusResponse> chattingList(@PathVariable("roomId") Long roomId, @AuthUser Member member) {
		chatService.updateCountAllZero(roomId, member.getEmail());
		ChatHistoryResDto response = chatService.getChattingList(roomId, member);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	// 채팅방 리스트 조회
	@GetMapping("/chatrooms")
	public ResponseEntity<StatusResponse> chatRoomList(@AuthUser Member member) {
		List<ChatRoomListResDto> response = chatService.getChatList(member);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	@MessageMapping("/message")
	public void sendMessage(@Valid Message message, @Header("Authorization") final String accessToken) {
		chatService.sendMessage(message, accessToken);
	}

	@MessageMapping("/updateMessage")
	public void updateMessage(@Valid Message updatedMessage, @Header("Authorization") final String accessToken) throws IOException {
		// 데이터베이스 상태 업데이트 또는 서비스 로직 실행
		chatService.updateMessage(updatedMessage, accessToken);
	}

	// 채팅방 접속 끊기
	@PostMapping("/chatrooms/{roomId}")
	public ResponseEntity<StatusResponse> disconnectChat(@PathVariable("roomId") Long roomId,
		@RequestParam("email") String email) {
		chatRoomService.disconnectChatRoom(roomId, email);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CHAT_ROOM_DISCONNECTED)
			.build());
	}


	// 메시지 전송 후 callback
	@PostMapping("/chatrooms/callback")
	public ResponseEntity<StatusResponse> sendNotification(@Valid @RequestBody Message message) {
		Message savedMessage = chatService.sendNotificationAndSaveMessage(message);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(savedMessage)
			.build());
	}
}
