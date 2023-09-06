package com.kuddy.apiserver.profile.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.InterestsDto;
import com.kuddy.apiserver.profile.dto.ProfileListResDto;
import com.kuddy.apiserver.profile.dto.ProfileReqDto;
import com.kuddy.apiserver.profile.dto.ProfileResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.domain.ProfileLanguage;
import com.kuddy.common.profile.exception.DuplicateProfileException;
import com.kuddy.common.profile.exception.ProfileNotFoundException;

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
		profile.updateAge(reqDto.getAge());
		setInterests(profile, reqDto.getInterests());
		profileLanguageService.updateProfileLanguage(profile, reqDto.getAvailableLanguages());
		profileAreaService.updateProfileDistrics(profile, reqDto.getDistricts());
		return profile;
	}

	public void setInterests(Profile profile, InterestsDto reqDto){

		profile.setActivitiesInvestmentTechs(reqDto.getActivitiesInvestmentTech());
		profile.setArtBeauties(reqDto.getArtBeauty());
		profile.setCareerMajors(reqDto.getCareerMajor());
		profile.setLifestyles(reqDto.getLifestyle());
		profile.setEntertainments(reqDto.getEntertainment());
		profile.setFoods(reqDto.getFood());
		profile.setHobbies(reqDto.getHobbiesInterests());
		profile.setSports(reqDto.getSports());
		profile.setWellbeing(reqDto.getWellbeing());
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
		List<ProfileResDto> respone = new ArrayList<>();
		for (Profile profile: profileList) {
			ProfileResDto profileResDto = ProfileResDto.from(profile.getMember(), profile);
			respone.add(profileResDto);
		}

		//페이지가 1장일 경우 요소의 총 개수가 size
		if (profilePage.getTotalPages() == 1) {
			size = (int) profilePage.getTotalElements();
		}

		PageInfo pageInfo = new PageInfo(page, size, profilePage.getTotalElements(),profilePage.getTotalPages());
		ProfileListResDto profileListResDto = new ProfileListResDto(respone, pageInfo);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(profileListResDto)
			.build());
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
