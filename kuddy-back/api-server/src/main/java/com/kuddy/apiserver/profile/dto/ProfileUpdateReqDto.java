package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.profile.domain.ActivitiesInvestmentTech;
import com.kuddy.common.profile.domain.ArtBeauty;
import com.kuddy.common.profile.domain.CareerMajor;
import com.kuddy.common.profile.domain.DecisionMaking;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.HobbiesInterests;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Sports;
import com.kuddy.common.profile.domain.Temperament;
import com.kuddy.common.profile.domain.Wellbeing;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUpdateReqDto {
	private String job;
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

	@Builder
	public ProfileUpdateReqDto(String job, Integer age, String nationality,
		DecisionMaking decisionMaking, Temperament temperament,
		ActivitiesInvestmentTech activitiesInvestmentTech, ArtBeauty artBeauty,
		CareerMajor careerMajor, Lifestyle lifestyle, Entertainment entertainment,
		Food food, GenderType genderType, HobbiesInterests hobbiesInterests,
		Sports sports, Wellbeing wellbeing, List<MemberAreaDto> districts,
		List<MemberLanguageDto> availableLanguages) {
		this.job = job;
		this.age = age;
		this.nationality = nationality;
		this.decisionMaking = decisionMaking;
		this.temperament = temperament;
		this.activitiesInvestmentTech = activitiesInvestmentTech;
		this.artBeauty = artBeauty;
		this.careerMajor = careerMajor;
		this.lifestyle = lifestyle;
		this.entertainment = entertainment;
		this.food = food;
		this.genderType = genderType;
		this.hobbiesInterests = hobbiesInterests;
		this.sports = sports;
		this.wellbeing = wellbeing;
		this.districts = districts;
		this.availableLanguages = availableLanguages;
	}
}
