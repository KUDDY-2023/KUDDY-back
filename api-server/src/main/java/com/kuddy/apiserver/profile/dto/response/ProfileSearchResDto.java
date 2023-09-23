package com.kuddy.apiserver.profile.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.apiserver.member.dto.MemberResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;
import com.kuddy.common.profile.domain.ProfileLanguage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileSearchResDto {
	private MemberResDto memberInfo;
	private String role;
	private String introduce;
	private GenderType gender;
	private String birthDate;
	private String temperament;
	private String decisionMaking;
	private String job;
	private InterestsDto interests;
	private String nationality;

	private List<MemberLanguageDto> languages;
	private List<MemberAreaDto> areas;
	private KuddyLevel kuddyLevel;
	private String ticketStatus;
	private boolean mine;


	private static List<MemberLanguageDto> buildLanguages(List<ProfileLanguage> availableLanguages) {
		List<MemberLanguageDto> languageList = new ArrayList<>();
		for (ProfileLanguage pl : availableLanguages) {
			MemberLanguageDto language = new MemberLanguageDto(pl.getLanguage().getType(), pl.getLaguageLevel());
			languageList.add(language);
		}
		return languageList;
	}

	private static List<MemberAreaDto> buildAreas(List<ProfileArea> districts) {
		List<MemberAreaDto> areaList = new ArrayList<>();
		for (ProfileArea pa : districts) {
			MemberAreaDto area = new MemberAreaDto(pa.getArea().getDistrict());
			areaList.add(area);
		}
		return areaList;
	}

	public static ProfileSearchResDto from(Member member, Profile profile) {
		KuddyLevel kuddyLevel = null;
		String ticketStatus = null;
		if(member.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		else{
			ticketStatus = profile.getTicketStatus().getDescription();
		}
		boolean checkMine = false;
		if(member.getId().equals(profile.getMember().getId())){
			checkMine = true;
		}

		return ProfileSearchResDto.builder()
			.memberInfo(MemberResDto.of(member))
			.role(member.getRoleType().getDisplayName())
			.introduce(profile.getIntroduce())
			.birthDate(profile.getBirthDate())
			.gender(profile.getGenderType())
			.job(profile.getJob())
			.temperament(profile.getTemperament().getName())
			.decisionMaking(profile.getDecisionMaking().getName())
			.interests(InterestsDto.of(profile))
			.nationality(profile.getNationality())
			.languages(buildLanguages(profile.getAvailableLanguages()))
			.areas(buildAreas(profile.getDistricts()))
			.ticketStatus(ticketStatus)
			.kuddyLevel(kuddyLevel)
			.mine(checkMine)
			.build();
	}
}
