package com.kuddy.common.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sports {
	FITNESS("1", "Fitness"),
	DANCE("2", "Dance"),
	BASKETBALL("3", "Basketball"),
	VOLLEYBALL("4", "Volleyball"),
	RUNNING("5", "Running"),
	SWIMMING("6", "Swimming"),
	BASEBALL("7", "Baseball"),
	BICYCLE("8", "Bicycle"),
	CLIMBING1("9", "Climbing"),
	BOWLING("10", "Bowling"),
	YOGA("11", "Yoga"),
	PILATES("12", "Pilates"),
	JOUJITSU("13", "Joujitsu"),
	GOLF("14", "Golf"),
	SURFING("15", "Surfing"),
	SKATEBOARD("16", "Skateboard"),
	SKI("17", "Ski"),
	CLIMBING2("18", "Climbing"),
	CROSSFIT("19", "Crossfit");

	private final String code;
	private final String name;

}
