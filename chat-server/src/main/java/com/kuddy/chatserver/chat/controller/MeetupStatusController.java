package com.kuddy.chatserver.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.chatserver.chat.dto.response.MeetupResDto;
import com.kuddy.chatserver.chat.service.MeetupStatusService;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat/v1/meetups")
@RequiredArgsConstructor
public class MeetupStatusController {
	private final MeetupStatusService meetupStatusService;
	@PutMapping("/{meetupId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> cancelMeetupStatus(@AuthUser Member member, @PathVariable Long meetupId){
		Meetup findMeetup = meetupStatusService.findByMeetupId(meetupId);
		Meetup cancelMeetup = meetupStatusService.cancel(member, findMeetup);
		MeetupResDto response = MeetupResDto.of(cancelMeetup, member);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}
}
