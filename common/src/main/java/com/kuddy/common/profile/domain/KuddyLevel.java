package com.kuddy.common.profile.domain;

import java.util.Arrays;

import com.kuddy.common.member.domain.MemberStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KuddyLevel {
	NOT_KUDDY("0", "NOT_KUDDY"),
	EXPLORER("1", "EXPLORER"),
	FRIEND("2", "FRIEND"),
	COMPANION("3", "COMPANION"),
	SOULMATE("4", "SOULMATE");

	private final String code;
	private final String name;

	public static KuddyLevel of(int code) {
		return Arrays.stream(KuddyLevel.values())
			.filter(r -> r.getCode().equals(String.valueOf(code)))
			.findAny()
			.orElse(EXPLORER);
	}
	public static String toString(KuddyLevel kuddyLevel){
		return kuddyLevel.name;
	}


}
