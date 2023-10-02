package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sports {
	NOT_SELECTED("0", "NOT_SELECTED"),
	FITNESS("1", "FITNESS"),
	DANCE("2", "DANCE"),
	BASKETBALL("3", "BASKETBALL"),
	VOLLEYBALL("4", "VOLLEYBALL"),
	RUNNING("5", "RUNNING"),
	SWIMMING("6", "SSWIMMING"),
	BASEBALL("7", "BASEBALL"),
	BICYCLE("8", "BICYCLE"),
	CLIMBING("9", "CLIMBING"),
	BOWLING("10", "BOWLING"),
	YOGA("11", "YOGA"),
	PILATES("12", "PILATES"),
	JIUJITSU("13", "JIUJITSU"),
	GOLF("14", "GOLF"),
	SURFING("15", "SURFING"),
	SKATEBOARD("16", "SKATEBOARD"),
	SKIING("17", "SKIING"),
	CROSSFIT("18", "CROSSFIT");

	private final String code;
	private final String name;

}
