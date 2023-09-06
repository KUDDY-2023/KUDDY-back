package com.kuddy.apiserver.profile.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.ProfileResDto;
import com.kuddy.apiserver.profile.service.ProfileService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileSearchController {
	private final ProfileService profileService;
	private final MemberService memberService;

	@GetMapping
	public ResponseEntity<StatusResponse> readProfile(@RequestParam final String nickname) {
		Member findMember = memberService.findByNickname(nickname);
		Profile profile = profileService.findByMember(findMember);
		ProfileResDto response = ProfileResDto.from(findMember, profile);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	@GetMapping("/kuddy")
	public ResponseEntity<StatusResponse> readKuddyProfile(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
		Page<Profile> profiles = profileService.getKuddyProfiles(page -1, size);
		return profileService.changePageToResponse(profiles, page, size);
	}

	@GetMapping("/traveler")
	public ResponseEntity<StatusResponse> readTravelerProfile(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
		Page<Profile> profiles = profileService.getTravelerProfiles(page -1, size);
		return profileService.changePageToResponse(profiles, page, size);
	}
}
