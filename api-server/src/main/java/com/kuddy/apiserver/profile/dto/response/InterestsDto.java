package com.kuddy.apiserver.profile.dto.response;

import java.util.List;

import com.kuddy.common.profile.domain.Art;
import com.kuddy.common.profile.domain.Career;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.Hobbies;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Sports;

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
	private List<Art> artBeauty;
	private List<Career> careerMajor;
	private List<Lifestyle> lifestyle;
	private List<Entertainment> entertainment;
	private List<Food> food;
	private List<Hobbies> hobbiesInterests;
	private List<Sports> sports;


	public static InterestsDto of(Profile profile) {
		return InterestsDto.builder()
			.artBeauty(profile.getArt())
			.hobbiesInterests(profile.getHobbies())
			.lifestyle(profile.getLifestyles())
			.careerMajor(profile.getCareers())
			.entertainment(profile.getEntertainments())
			.food(profile.getFoods())
			.sports(profile.getSports())
			.build();
	}
}
