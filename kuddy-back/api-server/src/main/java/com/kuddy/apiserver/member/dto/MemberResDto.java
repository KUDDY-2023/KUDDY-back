package com.kuddy.apiserver.member.dto;

import com.kuddy.common.member.domain.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResDto {
	private Long memberId;
	private String email;
	private String nickname;
	private String profileImageUrl;

	public static MemberResDto of(Member member){
		return MemberResDto.builder()
			.memberId(member.getId())
			.email(member.getEmail())
			.nickname(member.getNickname())
			.profileImageUrl(member.getProfileImageUrl())
			.build();
	}


}
