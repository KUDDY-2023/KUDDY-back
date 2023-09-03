package com.kuddy.common.profile.domain;

import java.util.Arrays;

import com.kuddy.common.member.domain.MemberStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KuddyLevel {
	NOT_KUDDY("0", "Not kuddy"),
	EXPLORER("1", "Explorer"),
	FRIENDZONE("2", "Friendzone"),
	COMPANION("3", "Companion"),
	HARMONY("4", "Harmony"),
	SOULMATE("5", "Soulmate");

	private final String code;
	private final String name;

	public static KuddyLevel of(int code) {
		return Arrays.stream(KuddyLevel.values())
			.filter(r -> r.getCode().equals(String.valueOf(code)))
			.findAny()
			.orElse(EXPLORER);
	}

}
