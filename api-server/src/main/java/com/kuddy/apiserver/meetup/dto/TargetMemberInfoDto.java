package com.kuddy.apiserver.meetup.dto;

import com.kuddy.common.member.domain.Member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetMemberInfoDto {
	private Long writerId;
	private String targetNickname;
	private String profileImageUrl;

	public TargetMemberInfoDto(Member member) {
		this.writerId = member.getId();
		this.targetNickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
	}
}
