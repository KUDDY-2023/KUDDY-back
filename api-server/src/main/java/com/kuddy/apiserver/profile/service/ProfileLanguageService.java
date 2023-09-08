package com.kuddy.apiserver.profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.profile.dto.response.MemberLanguageDto;
import com.kuddy.common.profile.domain.Language;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileLanguage;
import com.kuddy.common.profile.exception.LanguageNotFoundException;
import com.kuddy.common.profile.repository.LanguageRepository;
import com.kuddy.common.profile.repository.ProfileLanguageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProfileLanguageService {
	private final LanguageRepository languageRepository;
	private final ProfileLanguageRepository profileLanguageRepository;

	public List<ProfileLanguage> createProfileLanguage(Profile profile, List<MemberLanguageDto> languageReqDto){
		List<ProfileLanguage> languages = new ArrayList<>();
		for(MemberLanguageDto languageDto : languageReqDto) {
			Language language = findByType(languageDto.getLanguageType());
			ProfileLanguage profileLanguage = ProfileLanguage.builder()
				.profile(profile)
				.laguageLevel(languageDto.getLanguageLevel())
				.language(language)
				.build();
			profileLanguageRepository.save(profileLanguage);
			languages.add(profileLanguage);
		}
		return languages;
	}

	public void updateProfileLanguage(Profile profile, List<MemberLanguageDto> languageReqDto){
		for(MemberLanguageDto languageDto : languageReqDto) {
			Language language = findByType(languageDto.getLanguageType());
			Optional<ProfileLanguage> optionalProfileLanguage = findByProfileAnAndLanguage(profile, language);
			ProfileLanguage profileLanguage;

			if (optionalProfileLanguage.isPresent()) {
				profileLanguage = optionalProfileLanguage.get();
				profileLanguage.updateLanguageLevel(languageDto.getLanguageLevel());
			} else {
				profileLanguage = ProfileLanguage.builder()
					.profile(profile)
					.laguageLevel(languageDto.getLanguageLevel())
					.language(language)
					.build();
				profileLanguageRepository.save(profileLanguage);
			}
			profile.updateAvailableLanguages(profileLanguage);
		}
	}
	@Transactional(readOnly = true)
	public Language findByType(String type){
		return languageRepository.findByType(type).orElseThrow(LanguageNotFoundException::new);
	}
	@Transactional(readOnly = true)
	public Optional<ProfileLanguage> findByProfileAnAndLanguage(Profile profile, Language language){
		return profileLanguageRepository.findByProfileAndLanguage(profile, language);
	}
}
