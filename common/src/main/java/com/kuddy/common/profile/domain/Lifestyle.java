package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Lifestyle {
	NOT_SELECTED("0", "NOT_SELECTED"),
	SOCIALIZING("1", "SOCIALIZING"),
	HOMEBODY("2", "HOMEBODY"),
	WALK("3", "WALK"),
	PET("4", "PET"),
	FITNESS("5", "FITNESS"),
	CAMPING("6", "CAMPING"),
	PICNIC("7", "PICNIC"),
	VEGAN("8", "VEGAN"),
	VOLUNTEER("9", "VOLUNTEER");

	private final String code;
	private final String name;

}
