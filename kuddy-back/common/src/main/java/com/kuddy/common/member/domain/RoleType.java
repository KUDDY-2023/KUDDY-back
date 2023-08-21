package com.kuddy.common.member.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
	MEMBER("ROLE_USER", "member"),
	KUDDY("ROLE_KUDDY", "k-buddy"),
	TRAVELER("ROLE_TRAVELER", "traveler"),

	ADMIN("ROLE_ADMIN", "admin"),
	GUEST("GUEST", "guest");

	private final String code;
	private final String displayName;

	public static RoleType of(String code) {
		return Arrays.stream(RoleType.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElse(GUEST);
	}
}