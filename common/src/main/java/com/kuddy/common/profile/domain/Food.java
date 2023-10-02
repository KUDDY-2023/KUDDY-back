package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Food {
	NOT_SELECTED("0", "NOT_SELECTED"),
	KOREAN("1", "KOREAN"),
	CHINESE("2", "CHINESE"),
	ITALIAN("3", "ITALIAN"),
	MEXICAN("4", "MEXICAN"),
	JAPANESE("5", "JAPANESE"),
	COFFEE("6", "COFFEE"),
	DESSERT("7", "DESSERT"),
	DRINK("8", "DRINK");

	private final String code;
	private final String name;
}
