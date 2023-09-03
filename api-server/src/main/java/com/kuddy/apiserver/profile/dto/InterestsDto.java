package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.profile.domain.ActivitiesInvestmentTech;
import com.kuddy.common.profile.domain.ArtBeauty;
import com.kuddy.common.profile.domain.CareerMajor;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.HobbiesInterests;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Sports;
import com.kuddy.common.profile.domain.Wellbeing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterestsDto {
	private List<ActivitiesInvestmentTech> activitiesInvestmentTech;
	private List<ArtBeauty> artBeauty;
	private List<CareerMajor> careerMajor;
	private List<Lifestyle> lifestyle;
	private List<Entertainment> entertainment;
	private List<Food> food;
	private List<HobbiesInterests> hobbiesInterests;
	private List<Sports> sports;
	private List<Wellbeing> wellbeing;

	public static InterestsDto of(Profile profile) {
		return InterestsDto.builder()
			.activitiesInvestmentTech(profile.getActivitiesInvestmentTechs())
			.artBeauty(profile.getArtBeauties())
			.hobbiesInterests(profile.getHobbiesInterests())
			.lifestyle(profile.getLifestyles())
			.careerMajor(profile.getCareerMajors())
			.entertainment(profile.getEntertainments())
			.food(profile.getFoods())
			.wellbeing(profile.getWellbeings())
			.sports(profile.getSports())
			.build();
	}
}
