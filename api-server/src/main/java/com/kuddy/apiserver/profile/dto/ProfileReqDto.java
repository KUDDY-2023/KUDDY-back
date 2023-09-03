package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.DecisionMaking;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Temperament;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileReqDto {

	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Create {
		private String job;
		private RoleType roleType;
		private String nickname;
		private Integer age;
		private String nationality;
		private GenderType genderType;
		private DecisionMaking decisionMaking;
		private Temperament temperament;
		private InterestsDto interests;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;

		public Profile toEntity(Member member){
			return Profile.builder()
				.member(member)
				.age(age)
				.job(job)
				.genderType(genderType)
				.nationality(nationality)
				.temperament(temperament)
				.decisionMaking(decisionMaking)
				.build();
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Update {
		private String job;
		private Integer age;
		private String nickname;
		private String nationality;
		private DecisionMaking decisionMaking;
		private Temperament temperament;
		private GenderType genderType;
		private InterestsDto interests;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;

	}

}
