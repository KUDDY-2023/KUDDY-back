package com.kuddy.apiserver.profile.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kuddy.common.profile.domain.Profile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileSearchListResDto {
	private List<ProfileThumbnailResDto> profileList;
	private long totalProfileCount;

	public static ProfileSearchListResDto of(List<Profile> profiles) {
		List<ProfileThumbnailResDto> list = profiles.stream()
			.map(ProfileThumbnailResDto::of)
			.collect(Collectors.toList());

		return ProfileSearchListResDto.builder()
			.profileList(Collections.unmodifiableList(list))
			.totalProfileCount(list.size())
			.build();
	}

	public static ProfileSearchListResDto from(List<Profile> profiles, String searchContent) {
		List<ProfileThumbnailResDto> list = profiles.stream()
			.map(profile -> ProfileThumbnailResDto.from(profile, searchContent))
			.collect(Collectors.toList());

		return ProfileSearchListResDto.builder()
			.profileList(Collections.unmodifiableList(list))
			.totalProfileCount(list.size())
			.build();
	}

}
