package com.kuddy.apiserver.profile.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.response.InterestsDto;
import com.kuddy.apiserver.profile.dto.response.ProfileListResDto;
import com.kuddy.apiserver.profile.dto.request.ProfileReqDto;

import com.kuddy.apiserver.profile.dto.response.ProfileThumbnailResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.MemberStatus;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.domain.ProfileLanguage;
import com.kuddy.common.profile.exception.DuplicateProfileException;
import com.kuddy.common.profile.exception.ProfileNotFoundException;

import com.kuddy.common.profile.exception.WithdrawMemberProfileException;
import com.kuddy.common.profile.repository.ProfileRepository;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
	private final ProfileRepository profileRepository;
	private final ProfileLanguageService profileLanguageService;
	private final MemberService memberService;
	private final ProfileAreaService profileAreaService;
	private final ProfileQueryService profileQueryService;
	private final Top5KuddyService top5KuddyService;


	public Long create(Member member, ProfileReqDto.Create reqDto){
		if(existsProfileByMember(member)){
			throw new DuplicateProfileException();
		}
		Profile newProfile = reqDto.toEntity(member);
		Profile profile = profileRepository.save(newProfile);
		setInterests(profile, reqDto.getInterests());
		List<ProfileLanguage> profileLanguages = profileLanguageService.createProfileLanguage(profile, reqDto.getAvailableLanguages());
		List<ProfileArea> profileAreas = profileAreaService.createProfileArea(profile, reqDto.getDistricts());
		profile.setAvailableLanguages(profileLanguages);
		profile.setDistricts(profileAreas);
		profile.initKuddyLevel(reqDto.getRoleType());
		member.setProfile(profile);
		return profile.getId();
	}

	public Profile update(Member member, ProfileReqDto.Update reqDto) {
		Profile profile = findByMember(member);
		Member findMember = memberService.findById(member.getId());

		findMember.updateNickname(reqDto.getNickname());
		findMember.updateProfileImage(reqDto.getProfileImageUrl());
		profile.changeJob(reqDto.getJob());
		profile.updateIntroduce(reqDto.getIntroduce());
		profile.setNationality(reqDto.getNationality());
		profile.setGenderType(reqDto.getGenderType());
		profile.setDecisionMaking(reqDto.getDecisionMaking());
		profile.setTemperament(reqDto.getTemperament());
		profile.setGenderType(reqDto.getGenderType());
		profile.updateBirthDate(reqDto.getBirthDate());
		setInterests(profile, reqDto.getInterests());
		profileLanguageService.updateProfileLanguage(profile, reqDto.getAvailableLanguages());
		profileAreaService.updateProfileDistricts(profile, reqDto.getDistricts());
		top5KuddyService.updateTop5KuddiesCache(member);
		return profile;
	}


	public void setInterests(Profile profile, InterestsDto reqDto){
		profile.setArtBeauties(reqDto.getArtBeauty());
		profile.setCareers(reqDto.getCareerMajor());
		profile.setLifestyles(reqDto.getLifestyle());
		profile.setEntertainments(reqDto.getEntertainment());
		profile.setFoods(reqDto.getFood());
		profile.setHobbies(reqDto.getHobbiesInterests());
		profile.setSports(reqDto.getSports());
	}

	public Page<Profile> getKuddyProfiles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return profileQueryService.findAllExcludeNotKuddyOrderedByKuddyLevel(pageable);
	}
	public Page<Profile> getTravelerProfiles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return profileQueryService.findProfilesTravelerOrderedByCreatedDate(pageable);
	}

	public ResponseEntity<StatusResponse> changePageToResponse(Page<Profile> profilePage, int page, int size) {
		List<Profile> profileList = profilePage.getContent();
		List<ProfileThumbnailResDto> response = profileList.stream()
			.map(ProfileThumbnailResDto::of)
			.collect(Collectors.toList());

		//페이지가 1장일 경우 요소의 총 개수가 size
		if (profilePage.getTotalPages() == 1) {
			size = (int) profilePage.getTotalElements();
		}

		PageInfo pageInfo = new PageInfo(page, size, profilePage.getTotalElements(),profilePage.getTotalPages());
		ProfileListResDto profileListResDto = new ProfileListResDto(response, pageInfo);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(profileListResDto)
			.build());
	}

	public ResponseEntity<StatusResponse> changePageToSearchResponse(Page<Profile> profilePage, String searchInterestContent, int page, int size) {
		List<Profile> profileList = profilePage.getContent();
		List<ProfileThumbnailResDto> response = profileList.stream()
				.map(profile -> ProfileThumbnailResDto.from(profile, searchInterestContent))
				.collect(Collectors.toList());

		//페이지가 1장일 경우 요소의 총 개수가 size
		if (profilePage.getTotalPages() == 1) {
			size = (int) profilePage.getTotalElements();
		}

		PageInfo pageInfo = new PageInfo(page, size, profilePage.getTotalElements(),profilePage.getTotalPages());
		ProfileListResDto profileListResDto = new ProfileListResDto(response, pageInfo);

		return ResponseEntity.ok(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(profileListResDto)
				.build());
	}

	public void validateProfile(Profile profile){
		Member member = profile.getMember();
		if(member.getMemberStatus().equals(MemberStatus.WITHDRAW)){
			throw new WithdrawMemberProfileException();
		}
	}


	@Transactional(readOnly = true)
	public Profile findById(Long profileId){
		return profileRepository.findById(profileId).orElseThrow(ProfileNotFoundException::new);
	}
	@Transactional(readOnly = true)
	public Profile findByMember(Member member){
		return profileRepository.findByMember(member).orElseThrow(ProfileNotFoundException::new);
	}
	@Transactional(readOnly = true)
	public boolean existsProfileByMember(Member member){
		return profileRepository.existsByMember(member);
	}

}
