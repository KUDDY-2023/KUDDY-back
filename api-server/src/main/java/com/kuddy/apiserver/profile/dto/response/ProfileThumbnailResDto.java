package com.kuddy.apiserver.profile.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.ActivitiesInvestmentTech;
import com.kuddy.common.profile.domain.ArtBeauty;
import com.kuddy.common.profile.domain.CareerMajor;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.HobbiesInterests;
import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Sports;
import com.kuddy.common.profile.domain.Wellbeing;

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
public class ProfileThumbnailResDto {
	private Long profileId;
	private Long memberId;
	private RoleType role;
	private String introduce;
	private String nickname;
	private String profileImage;
	private KuddyLevel kuddyLevel;
	private List<String> seletedInterests;

	public static ProfileThumbnailResDto of(Profile profile) {
		Member owner = profile.getMember();
		KuddyLevel kuddyLevel = null;
		if(owner.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		// 각 리스트를 스트림으로 변환하고 NOT_SELECTED를 제거
		Stream<ActivitiesInvestmentTech> investmentTechStream = profile.getActivitiesInvestmentTechs().stream()
			.filter(it -> !it.equals(ActivitiesInvestmentTech.NOT_SELECTED));

		Stream<ArtBeauty> artBeautyStream = profile.getArtBeauties().stream()
			.filter(it -> !it.equals(ArtBeauty.NOT_SELECTED));

		Stream<CareerMajor> careerMajorStream = profile.getCareerMajors().stream()
			.filter(it -> !it.equals(CareerMajor.NOT_SELECTED));
		Stream<Lifestyle> lifestyleStream = profile.getLifestyles().stream()
			.filter(it -> !it.equals(Lifestyle.NOT_SELECTED));

		Stream<Entertainment> entertainmentStream = profile.getEntertainments().stream()
			.filter(it -> !it.equals(Entertainment.NOT_SELECTED));

		Stream<Food> foodStream = profile.getFoods().stream()
			.filter(it -> !it.equals(Food.NOT_SELECTED));

		Stream<HobbiesInterests> hobbiesInterestsStream = profile.getHobbiesInterests().stream()
			.filter(it -> !it.equals(HobbiesInterests.NOT_SELECTED));

		Stream<Sports> sportsStream = profile.getSports().stream()
			.filter(it -> !it.equals(Sports.NOT_SELECTED));

		Stream<Wellbeing> wellbeingStream = profile.getWellbeings().stream()
			.filter(it -> !it.equals(Wellbeing.NOT_SELECTED));

		// 모든 스트림을 하나로 병합하고 String으로 변환한 뒤 리스트로 변환합니다.
		List<String> allInterests = Stream.of(
				investmentTechStream, artBeautyStream, careerMajorStream,
				lifestyleStream, entertainmentStream, foodStream,
				hobbiesInterestsStream, sportsStream, wellbeingStream)
			.flatMap(it -> it)
			.map(Enum::name) // Enum을 String으로 변환
			.collect(Collectors.toList());


		Random rand = new Random();
		List<String> selectedInterests = new ArrayList<>();
		for (int i = 0; i < 3 && !allInterests.isEmpty(); i++) {
			int randomIndex = rand.nextInt(allInterests.size());
			selectedInterests.add(allInterests.remove(randomIndex));
		}
		return ProfileThumbnailResDto.builder()
			.profileId(profile.getId())
			.memberId(owner.getId())
			.introduce(profile.getIntroduce())
			.profileImage(owner.getProfileImageUrl())
			.kuddyLevel(kuddyLevel)
			.role(owner.getRoleType())
			.nickname(owner.getNickname())
			.seletedInterests(selectedInterests)
			.build();
	}

	public static ProfileThumbnailResDto from(Profile profile, String searchContent) {
		Member owner = profile.getMember();
		KuddyLevel kuddyLevel = null;
		if(owner.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		// 각 리스트를 스트림으로 변환하고 NOT_SELECTED를 제거
		Stream<ActivitiesInvestmentTech> investmentTechStream = profile.getActivitiesInvestmentTechs().stream()
			.filter(it -> !it.equals(ActivitiesInvestmentTech.NOT_SELECTED));

		Stream<ArtBeauty> artBeautyStream = profile.getArtBeauties().stream()
			.filter(it -> !it.equals(ArtBeauty.NOT_SELECTED));

		Stream<CareerMajor> careerMajorStream = profile.getCareerMajors().stream()
			.filter(it -> !it.equals(CareerMajor.NOT_SELECTED));
		Stream<Lifestyle> lifestyleStream = profile.getLifestyles().stream()
			.filter(it -> !it.equals(Lifestyle.NOT_SELECTED));

		Stream<Entertainment> entertainmentStream = profile.getEntertainments().stream()
			.filter(it -> !it.equals(Entertainment.NOT_SELECTED));

		Stream<Food> foodStream = profile.getFoods().stream()
			.filter(it -> !it.equals(Food.NOT_SELECTED));

		Stream<HobbiesInterests> hobbiesInterestsStream = profile.getHobbiesInterests().stream()
			.filter(it -> !it.equals(HobbiesInterests.NOT_SELECTED));

		Stream<Sports> sportsStream = profile.getSports().stream()
			.filter(it -> !it.equals(Sports.NOT_SELECTED));

		Stream<Wellbeing> wellbeingStream = profile.getWellbeings().stream()
			.filter(it -> !it.equals(Wellbeing.NOT_SELECTED));

		// 모든 스트림을 하나로 병합하고 String으로 변환한 뒤 리스트로 변환합니다.
		List<String> allInterests = Stream.of(
				investmentTechStream, artBeautyStream, careerMajorStream,
				lifestyleStream, entertainmentStream, foodStream,
				hobbiesInterestsStream, sportsStream, wellbeingStream)
			.flatMap(it -> it)
			.map(Enum::name) // Enum을 String으로 변환
			.collect(Collectors.toList());


		Random rand = new Random();
		List<String> selectedInterests = new ArrayList<>();
		selectedInterests.add(searchContent);
		for (int i = 0; i < 2 && !allInterests.isEmpty(); i++) {
			int randomIndex = rand.nextInt(allInterests.size());
			selectedInterests.add(allInterests.remove(randomIndex));
		}
		return ProfileThumbnailResDto.builder()
			.profileId(profile.getId())
			.memberId(owner.getId())
			.introduce(profile.getIntroduce())
			.profileImage(owner.getProfileImageUrl())
			.kuddyLevel(kuddyLevel)
			.role(owner.getRoleType())
			.nickname(owner.getNickname())
			.seletedInterests(selectedInterests)
			.build();
	}
}

