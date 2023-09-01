package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.ActivitiesInvestmentTech;
import com.kuddy.common.profile.domain.ArtBeauty;
import com.kuddy.common.profile.domain.CareerMajor;
import com.kuddy.common.profile.domain.DecisionMaking;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.HobbiesInterests;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Sports;
import com.kuddy.common.profile.domain.Temperament;
import com.kuddy.common.profile.domain.Wellbeing;

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
		private DecisionMaking decisionMaking;
		private Temperament temperament;
		private ActivitiesInvestmentTech activitiesInvestmentTech;
		private ArtBeauty artBeauty;
		private CareerMajor careerMajor;
		private Lifestyle lifestyle;
		private Entertainment entertainment;
		private Food food;
		private GenderType genderType;
		private HobbiesInterests hobbiesInterests;
		private Sports sports;
		private Wellbeing wellbeing;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;

		public Profile toEntity(Member member){
			return Profile.builder()
				.member(member)
				.activitiesInvestmentTech(activitiesInvestmentTech)
				.age(age)
				.artBeauty(artBeauty)
				.careerMajor(careerMajor)
				.decisionMaking(decisionMaking)
				.entertainment(entertainment)
				.food(food)
				.hobbiesInterests(hobbiesInterests)
				.genderType(genderType)
				.lifestyle(lifestyle)
				.wellbeing(wellbeing)
				.nationality(nationality)
				.sports(sports)
				.temperament(temperament)
				.hobbiesInterests(hobbiesInterests)
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
		private ActivitiesInvestmentTech activitiesInvestmentTech;
		private ArtBeauty artBeauty;
		private CareerMajor careerMajor;
		private Lifestyle lifestyle;
		private Entertainment entertainment;
		private Food food;
		private GenderType genderType;
		private HobbiesInterests hobbiesInterests;
		private Sports sports;
		private Wellbeing wellbeing;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;


	}

}
