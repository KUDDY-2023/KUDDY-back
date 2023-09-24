package com.kuddy.common.member.domain;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
	MEMBER("ROLE_USER", "MEMBER"),
	KUDDY("ROLE_KUDDY", "KUDDY"),
	READY_TO_BE_KUDDY("ROLE_READY_TO_BE_KUDDY", "READY_TO_BE_KUDDY"),
	TRAVELER("ROLE_TRAVELER", "TRAVELER"),

	ADMIN("ROLE_ADMIN", "ADMIN"),
	GUEST("GUEST", "GUEST");

	private final String code;
	private final String displayName;

	public static RoleType of(String code) {
		return Arrays.stream(RoleType.values())
			.filter(r -> r.getCode().equalsIgnoreCase(code))
			.findAny()
			.orElse(GUEST);
	}
	public static RoleType fromString(String roleString) {
		if (roleString == null || roleString.trim().isEmpty()) {
			return null;
		}

		try {
			return RoleType.valueOf(roleString.toUpperCase().trim());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
