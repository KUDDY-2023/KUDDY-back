package com.kuddy.apiserver.profile.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kuddy.apiserver.meetup.dto.MeetupListResDto;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
