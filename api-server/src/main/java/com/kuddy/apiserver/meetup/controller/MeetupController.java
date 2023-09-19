package com.kuddy.apiserver.meetup.controller;

import java.util.List;

import com.kuddy.apiserver.meetup.service.MeetupReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.apiserver.meetup.dto.MeetupListResDto;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/meetups")
@RequiredArgsConstructor
public class MeetupController {
	private final MeetupService meetupService;
	private final MeetupReviewService meetupReviewService;

	@GetMapping
	public ResponseEntity<StatusResponse> getMeetupList(@AuthUser Member member) {
		List<Meetup> meetupList = meetupService.findListByMember(member);
		MeetupListResDto response = MeetupListResDto.from(meetupList, member);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}


	//리뷰 요청 모달 API
	@GetMapping("/review")
    public ResponseEntity<StatusResponse> getNotReviewedMeetupList(@AuthUser Member member) {
		return meetupReviewService.checkReviewByMember(member);
	}

}
