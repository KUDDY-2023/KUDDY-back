package com.kuddy.apiserver.profile.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.apiserver.profile.dto.InterestsDto;
import com.kuddy.apiserver.profile.dto.ProfileReqDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.domain.ProfileLanguage;
import com.kuddy.common.profile.exception.DuplicateProfileException;
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
		profile.changeJob(reqDto.getJob());
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

	private void setInterests(Profile profile, InterestsDto reqDto){

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
