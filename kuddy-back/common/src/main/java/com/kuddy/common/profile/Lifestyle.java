package com.kuddy.common.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Lifestyle {
	LIGHTHEARTED("1", "Lighthearted"),
	MAKING_FRIENDS("2", "Making Friends"),
	STAYING_HOME("3", "Staying Home"),
	DATING("4", "Dating"),
	CAT_BUTLER("5", "Cat Butler"),
	WALK("6", "Walk"),
	DOG("7", "Dog"),
	FAN("8", "Fan"),
	PET("9", "Pet"),
	DIET("10", "Diet"),
	MARRIAGE("11", "Marriage"),
	CHILD_CARE("12", "Child Care");

	private final String code;
	private final String name;

}
