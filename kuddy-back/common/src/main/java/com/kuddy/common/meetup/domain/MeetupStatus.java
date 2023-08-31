package com.kuddy.common.meetup.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MeetupStatus {
	NOT_ACCEPT("0", "Not_Accepted"),
	ACCEPTED("1", "Accepted"),
	REFUSED("2", "Refused"),
	KUDDY_CANCEL("3", "Kuddy_Canceled"),
	TRAVELER_CANCEL("4","Traveler_Canceled"),
	COMPLETED("4", "Completed"),
	PAYED("5", "Payed");
	private final String code;
	private final String name;

	public static MeetupStatus fromString(String name) {
		for (MeetupStatus value : MeetupStatus.values()) {
			if (value.name.equalsIgnoreCase(name)) {
				return value;
			}
		}
		return NOT_ACCEPT;
	}
}
