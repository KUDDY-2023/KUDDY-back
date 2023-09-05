package com.kuddy.apiserver.meetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TargetMemberInfoDto {
	private Long writerId;
	private String targetNickname;
	private String profileImageUrl;
}
