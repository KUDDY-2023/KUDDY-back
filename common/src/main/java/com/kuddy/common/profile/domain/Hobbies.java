package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Hobbies {
	NOT_SELECTED("0", "NOT_SELECTED"),
	TRAVEL("1", "TRAVEL"),
	LANGUAGE("2", "LANGUAGE"),
	READING("3", "READING"),
	SHOPPING("4", "SHOPPING"),
	DRIVING("5", "DRIVING"),
	DIY("6", "DIY");

	private final String code;
	private final String name;
}
