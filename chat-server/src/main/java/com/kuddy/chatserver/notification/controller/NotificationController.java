package com.kuddy.chatserver.notification.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.chatserver.chat.service.ChatRoomService;
import com.kuddy.chatserver.notification.dto.ChatNotiResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final ChatRoomService chatRoomService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> getTotalUnreadMessages(@AuthUser Member member) {
		Long totalUnreadMessages = chatRoomService.getTotalUnreadMessages(member);
		ChatNotiResDto response = new ChatNotiResDto(totalUnreadMessages);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

}
