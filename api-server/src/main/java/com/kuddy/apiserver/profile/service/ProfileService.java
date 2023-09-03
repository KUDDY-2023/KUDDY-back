package com.kuddy.apiserver.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.ProfileReqDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.domain.ProfileLanguage;
import com.kuddy.common.profile.exception.ProfileNotFoundException;

import com.kuddy.common.profile.repository.ProfileRepository;

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

	public Long create(Member member, ProfileReqDto.Create reqDto){
		Profile newProfile = reqDto.toEntity(member);
		Profile profile = profileRepository.save(newProfile);

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
		profile.changeJob(reqDto.getJob());
		profile.setNationality(reqDto.getNationality());
		profile.setDecisionMaking(reqDto.getDecisionMaking());
		profile.setTemperament(reqDto.getTemperament());

		profile.setActivitiesInvestmentTech(reqDto.getActivitiesInvestmentTech());
		profile.setArtBeauty(reqDto.getArtBeauty());
		profile.setCareerMajor(reqDto.getCareerMajor());
		profile.setLifestyle(reqDto.getLifestyle());
		profile.setEntertainment(reqDto.getEntertainment());
		profile.setFood(reqDto.getFood());
		profile.setGenderType(reqDto.getGenderType());
		profile.setHobby(reqDto.getHobbiesInterests());
		profile.setSports(reqDto.getSports());
		profile.setWellbeing(reqDto.getWellbeing());
		profile.updateAge(reqDto.getAge());
		profileLanguageService.updateProfileLanguage(profile, reqDto.getAvailableLanguages());
		profileAreaService.updateProfileDistrics(profile, reqDto.getDistricts());
		return profile;
	}


	@Transactional(readOnly = true)
	public Profile findById(Long profileId){
		return profileRepository.findById(profileId).orElseThrow(ProfileNotFoundException::new);
	}
	@Transactional(readOnly = true)
	public Profile findByMember(Member member){
		return profileRepository.findByMember(member).orElseThrow(ProfileNotFoundException::new);
	}

}
