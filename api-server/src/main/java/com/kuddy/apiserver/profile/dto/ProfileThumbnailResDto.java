package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Profile;

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

	public static ProfileThumbnailResDto of(Profile profile) {
		Member owner = profile.getMember();
		KuddyLevel kuddyLevel = null;
		if(owner.getRoleType().equals(RoleType.KUDDY)){
			kuddyLevel =  profile.getKuddyLevel();
		}
		return ProfileThumbnailResDto.builder()
			.profileId(profile.getId())
			.memberId(owner.getId())
			.introduce(profile.getIntroduce())
			.profileImage(owner.getProfileImageUrl())
			.kuddyLevel(kuddyLevel)
			.role(owner.getRoleType())
			.nickname(owner.getNickname())
			.build();
	}
}

