package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Art {
	NOT_SELECTED("0", "NOT_SELECTED"),
	FASHION("1", "FASHION"),
	BEAUTY("2", "BEAUTY"),
	DESIGN("3", "DESIGN"),
	PICTURE("4", "PICTURE"),
	EXHIBITION("5", "EXHIBITION"),
	ROOM_INTERIOR("6", "ROOM_INTERIOR");

	private final String code;
	private final String name;
}
