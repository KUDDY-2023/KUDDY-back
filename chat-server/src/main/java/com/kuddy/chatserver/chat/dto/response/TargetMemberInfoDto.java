package com.kuddy.chatserver.chat.dto.response;

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
	private String targetEmail;

	public TargetMemberInfoDto(Member member) {
		this.writerId = member.getId();
		this.targetNickname = member.getNickname();
		this.profileImageUrl = member.getProfileImageUrl();
		this.targetEmail = member.getEmail();
	}
}
