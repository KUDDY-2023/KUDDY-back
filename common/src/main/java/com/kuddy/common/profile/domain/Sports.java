package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sports {
	NOT_SELECTED("0", "NOT_SELECTED"),
	DANCE("1", "DANCE"),
	BASKETBALL("2", "BASKETBALL"),
	VOLLEYBALL("3", "VOLLEYBALL"),
	RUNNING("4", "RUNNING"),
	SWIMMING("5", "SWIMMING"),
	BASEBALL("6", "BASEBALL"),
	BICYCLE("7", "BICYCLE"),
	CLIMBING("8", "CLIMBING"),
	BOWLING("9", "BOWLING"),
	YOGA("10", "YOGA"),
	PILATES("11", "PILATES"),
	JIUJITSU("12", "JIUJITSU"),
	GOLF("13", "GOLF"),
	SURFING("14", "SURFING"),
	SKATEBOARD("15", "SKATEBOARD"),
	SKIING("16", "SKIING"),
	CROSSFIT("17", "CROSSFIT");

	private final String code;
	private final String name;

}
