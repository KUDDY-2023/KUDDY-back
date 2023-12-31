package com.kuddy.apiserver.profile.controller;

import java.util.List;

import com.kuddy.apiserver.profile.dto.response.Top5KuddyListResDto;
import com.kuddy.apiserver.profile.service.Top5KuddyService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.request.ProfileSearchReqDto;
import com.kuddy.apiserver.profile.dto.response.ProfileResDto;
import com.kuddy.apiserver.profile.dto.response.ProfileSearchListResDto;
import com.kuddy.apiserver.profile.service.ProfileQueryService;
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
	private final ProfileQueryService profileQueryService;
	private final MemberService memberService;
	private final Top5KuddyService top5KuddyService;

	@GetMapping
	public ResponseEntity<StatusResponse> readProfile(@RequestParam final String nickname) {
		Member findMember = memberService.findByNickname(nickname);
		Profile profile = profileService.findByMember(findMember);
		profileService.validateProfile(profile);
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

	@GetMapping("kuddy/top5")
	public ResponseEntity<StatusResponse> readTop5KuddyProfile() {
		Top5KuddyListResDto response = top5KuddyService.getTop5Kuddy();
		return ResponseEntity.ok(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(response)
				.build());
	}

	@GetMapping("/traveler")
	public ResponseEntity<StatusResponse> readTravelerProfile(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
		Page<Profile> profiles = profileService.getTravelerProfiles(page -1, size);
		return profileService.changePageToResponse(profiles, page, size);
	}

	@GetMapping("/search")
	public ResponseEntity<StatusResponse> searchNickname(@RequestParam final String nickname) {
		List<Profile> profileList = profileQueryService.findProfilesByMemberNickname(nickname);
		ProfileSearchListResDto response = ProfileSearchListResDto.of(profileList);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}

	@PostMapping("/search")
	public ResponseEntity<StatusResponse> searchProfileInfo(
			@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
			@RequestBody final ProfileSearchReqDto reqDto) {
		Page<Profile> profileList = profileQueryService.findProfilesBySearchCriteria(page, size, reqDto);
		return profileService.changePageToSearchResponse(profileList, reqDto.getInterestContent(), page, size);
	}
}
