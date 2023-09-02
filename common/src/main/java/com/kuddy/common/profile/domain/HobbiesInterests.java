package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HobbiesInterests {
	NOT_SELECTED("0", "Not Selected"),
	TRAVEL("1", "Travel"),
	LANGUAGE_EXCHANGE("2", "Language Exchange"),
	LANGUAGE("3", "Language"),
	READING("4", "Reading"),
	SHOPPING("5", "Shopping"),
	DRIVE("6", "Drive"),
	DIY("7", "DIY");

	private final String code;
	private final String name;
}
