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
public class ProfileResDto {
	private MemberResDto memberInfo;
	private String role;
	private String introduce;
	private GenderType gender;
	private Integer age;
	private String temperament;
	private String decisionMaking;
	private String job;
	private InterestsDto interests;
	private String nationality;

	private List<MemberLanguageDto> languages;
	private List<MemberAreaDto> areas;
	private KuddyLevel kuddyLevel;
	private String ticketStatus;

	public static ProfileResDto from(Member member, Profile profile) {
		List<MemberLanguageDto> languageList = new ArrayList<>();
		for(ProfileLanguage pl : profile.getAvailableLanguages()) {
			MemberLanguageDto language = new MemberLanguageDto(pl.getLanguage().getType(), pl.getLaguageLevel());
			languageList.add(language);
		}

		List<MemberAreaDto> areaList = new ArrayList<>();
		for(ProfileArea pa : profile.getDistricts()){
			MemberAreaDto area = new MemberAreaDto(pa.getArea().getDistrict());
			areaList.add(area);
		}
		KuddyLevel kuddyLevel = null;
		String ticketStatus = null;
		if(member.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		else{
			ticketStatus = profile.getTicketStatus().getDescription();
		}

		return ProfileResDto.builder()
			.role(member.getRoleType().getDisplayName())
			.memberInfo(MemberResDto.of(member))
			.introduce(profile.getIntroduce())
			.age(profile.getAge())
			.gender(profile.getGenderType())
			.temperament(profile.getTemperament().getName())
			.decisionMaking(profile.getDecisionMaking().getName())
			.interests(InterestsDto.of(profile))
			.nationality(profile.getNationality())
			.languages(languageList)
			.areas(areaList)
			.ticketStatus(ticketStatus)
			.kuddyLevel(kuddyLevel)
			.build();
	}

}

