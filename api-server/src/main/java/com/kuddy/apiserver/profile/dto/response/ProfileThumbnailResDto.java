package com.kuddy.apiserver.profile.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.Art;
import com.kuddy.common.profile.domain.Career;
import com.kuddy.common.profile.domain.Entertainment;
import com.kuddy.common.profile.domain.Food;
import com.kuddy.common.profile.domain.Hobbies;
import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Lifestyle;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Sports;

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
	private List<String> allInterests;

	public static ProfileThumbnailResDto of(Profile profile) {
		Member owner = profile.getMember();
		KuddyLevel kuddyLevel = null;
		if(owner.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		// 각 리스트를 스트림으로 변환하고 NOT_SELECTED를 제거
		Stream<Art> artStream = filterNotSelected(profile.getArt().stream(), Art.NOT_SELECTED);
		Stream<Career> careerStream = filterNotSelected(profile.getCareers().stream(), Career.NOT_SELECTED);
		Stream<Lifestyle> lifestyleStream = filterNotSelected(profile.getLifestyles().stream(), Lifestyle.NOT_SELECTED);
		Stream<Entertainment> entertainmentStream = filterNotSelected(profile.getEntertainments().stream(), Entertainment.NOT_SELECTED);
		Stream<Food> foodStream = filterNotSelected(profile.getFoods().stream(), Food.NOT_SELECTED);
		Stream<Hobbies> hobbiesStream = filterNotSelected(profile.getHobbies().stream(), Hobbies.NOT_SELECTED);
		Stream<Sports> sportsStream = filterNotSelected(profile.getSports().stream(), Sports.NOT_SELECTED);



		// 모든 스트림을 하나로 병합하고 String으로 변환한 뒤 리스트로 변환합니다.
		List<String> allInterests = Stream.of(
				artStream, careerStream,
				lifestyleStream, entertainmentStream, foodStream,
				hobbiesStream, sportsStream)
			.flatMap(it -> it)
			.map(Enum::name) // Enum을 String으로 변환
			.collect(Collectors.toList());

		return ProfileThumbnailResDto.builder()
			.profileId(profile.getId())
			.memberId(owner.getId())
			.introduce(profile.getIntroduce())
			.profileImage(owner.getProfileImageUrl())
			.kuddyLevel(kuddyLevel)
			.role(owner.getRoleType())
			.nickname(owner.getNickname())
			.allInterests(allInterests)
			.build();
	}

	public static ProfileThumbnailResDto from(Profile profile,String searchContent) {
		Member owner = profile.getMember();
		KuddyLevel kuddyLevel = null;
		if(owner.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}

		Stream<Art> artStream = filterNotSelected(profile.getArt().stream(), Art.NOT_SELECTED);
		Stream<Career> careerStream = filterNotSelected(profile.getCareers().stream(), Career.NOT_SELECTED);
		Stream<Lifestyle> lifestyleStream = filterNotSelected(profile.getLifestyles().stream(), Lifestyle.NOT_SELECTED);
		Stream<Entertainment> entertainmentStream = filterNotSelected(profile.getEntertainments().stream(), Entertainment.NOT_SELECTED);
		Stream<Food> foodStream = filterNotSelected(profile.getFoods().stream(), Food.NOT_SELECTED);
		Stream<Hobbies> hobbiesInterestsStream = filterNotSelected(profile.getHobbies().stream(), Hobbies.NOT_SELECTED);
		Stream<Sports> sportsStream = filterNotSelected(profile.getSports().stream(), Sports.NOT_SELECTED);



		// 모든 스트림을 하나로 병합하고 String으로 변환한 뒤 리스트로 변환합니다.
		List<String> allInterests = Stream.of(
				artStream, careerStream,
				lifestyleStream, entertainmentStream, foodStream,
				hobbiesInterestsStream, sportsStream)
			.flatMap(it -> it)
			.map(Enum::name) // Enum을 String으로 변환
			.collect(Collectors.toList());
		if (!searchContent.isBlank()) {
			allInterests.remove(searchContent);
			allInterests.add(0, searchContent);
		}

		return ProfileThumbnailResDto.builder()
			.profileId(profile.getId())
			.memberId(owner.getId())
			.introduce(profile.getIntroduce())
			.profileImage(owner.getProfileImageUrl())
			.kuddyLevel(kuddyLevel)
			.role(owner.getRoleType())
			.nickname(owner.getNickname())
			.allInterests(allInterests)
			.build();
	}

	private static <E extends Enum<E>> Stream<E> filterNotSelected(Stream<E> stream, E notSelected) {
		return stream.filter(it -> !it.equals(notSelected));
	}
}

