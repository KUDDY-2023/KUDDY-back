package com.kuddy.common.meetup.domain;

import java.util.Arrays;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetupStatus {
	NOT_ACCEPT("0", "NOT_ACCEPT"),
	ACCEPTED("1", "ACCEPTED"),
	REFUSED("2", "REFUSED"),
	KUDDY_CANCEL("3", "KUDDY_CANCEL"),
	TRAVELER_CANCEL("4","TRAVELER_CANCEL"),
	COMPLETED("5", "COMPLETED"),
	PAYED("6", "PAYED"),
	UNKNOWN("-1", "UNKNOWN");
	private final String code;
	private final String name;

	public static MeetupStatus fromString(String name) {
		return Arrays.stream(MeetupStatus.values())
			.filter(r -> r.getName().equalsIgnoreCase(name))
			.findAny()
			.orElse(NOT_ACCEPT);
	}


}
