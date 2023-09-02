package com.kuddy.common.member.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
	REGISTERED("0", "Registered"),
	WITHDRAW("1", "Withdraw"),
	BLOCKED("2", "Blocked");

	private final String code;
	private final String displayName;

	public static MemberStatus of(String code) {
		return Arrays.stream(MemberStatus.values())
			.filter(r -> r.getCode().equalsIgnoreCase(code))
			.findAny()
			.orElse(REGISTERED);
	}
}
