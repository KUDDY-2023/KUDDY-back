package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Wellbeing {
	HEALTH("1", "Health"),
	CAMPING("2", "Camping"),
	MEDITATION("3", "Meditation"),
	PICNIC("4", "Picnic"),
	ENVIRONMENTAL("5", "Environmental"),
	PROTECTION("6", "Protection"),
	VEGAN("7", "Vegan"),
	VOLUNTEER("8", "Volunteer");


	private final String code;
	private final String name;
}
