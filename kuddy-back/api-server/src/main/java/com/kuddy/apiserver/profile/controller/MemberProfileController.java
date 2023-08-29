package com.kuddy.apiserver.profile.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.ProfileReqDto;
import com.kuddy.apiserver.profile.dto.ProfileResDto;
import com.kuddy.apiserver.profile.service.ProfileService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members/profile")
@RequiredArgsConstructor
public class MemberProfileController {

	private final ProfileService profileService;
	private final MemberService memberService;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> readProfile(@AuthUser Member member) {
		Profile profile = profileService.findByMember(member);
		ProfileResDto response = ProfileResDto.of(member, profile);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> createProfile(@AuthUser Member member,
		@Valid @RequestBody final ProfileReqDto.Create profileCreateReqDto) {
		Member updateMember = memberService.update(member, profileCreateReqDto.getNickname(), profileCreateReqDto.getRoleType());
		Long profileId = profileService.create(updateMember, profileCreateReqDto);
		Profile createProfile = profileService.findById(profileId);
		ProfileResDto response = ProfileResDto.of(updateMember, createProfile);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(profileId)
			.toUri();

		return ResponseEntity.created(location)
			.body(StatusResponse.builder()
				.status(StatusEnum.CREATED.getStatusCode())
				.message(StatusEnum.CREATED.getCode())
				.data(response)
				.build());
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> updateProfile(@AuthUser Member member,
		@Valid @RequestBody final ProfileReqDto.Update profileReqDto) {
		Profile updateProfile = profileService.update(member, profileReqDto);
		ProfileResDto response = ProfileResDto.of(member, updateProfile);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

}
