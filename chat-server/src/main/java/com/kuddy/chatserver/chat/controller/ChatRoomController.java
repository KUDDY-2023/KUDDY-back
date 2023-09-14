package com.kuddy.chatserver.chat.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/chat/v1/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private static final String CHAT_ROOM_DISCONNECTED= "성공적으로 접속 해제했습니다.";


	@PostMapping
	@PreAuthorize("isAuthenticated()")
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

	// 채팅방 리스트 조회
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> chatRoomList(@AuthUser Member member) {
		List<ChatRoomListResDto> response = chatService.getChatList(member);
		return ResponseEntity.ok(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(response)
				.build());
	}

	// 채팅내역 조회 AuthUSER
	@GetMapping("/{roomId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> chattingList(@PathVariable("roomId") Long roomId, @AuthUser Member member) {
		String email = chatService.checkRoomIdOwnerValidation(member, roomId);
		chatService.updateCountAllZero(roomId, email);
		ChatHistoryResDto response = chatService.getChattingList(roomId, member);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}


	@GetMapping("/check")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> hasChatRoom(@AuthUser Member member,@RequestParam("email") String email) {
		Room room = chatService.findByMembers(member, email);
		ChatRoomResDto response = new ChatRoomResDto(room);
		return ResponseEntity.ok(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(response)
				.build());
	}


	// 채팅방 접속 끊기
	@DeleteMapping("/{roomId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> disconnectChat(@PathVariable("roomId") Long roomId,
		@RequestParam("email") String email, @AuthUser Member member) {
		String loginEmail = chatService.checkRoomIdOwnerValidation(member, roomId);
		chatService.checkEmailValidation(loginEmail, email);
		chatRoomService.disconnectChatRoom(roomId, loginEmail);
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
