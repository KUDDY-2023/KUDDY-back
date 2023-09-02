package com.kuddy.common.meetup.domain;

import java.util.Arrays;

import com.kuddy.common.member.domain.RoleType;

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
	COMPLETED("5", "Completed"),
	PAYED("6", "Payed");
	private final String code;
	private final String name;

	public static MeetupStatus fromString(String name) {
		return Arrays.stream(MeetupStatus.values())
			.filter(r -> r.getCode().equalsIgnoreCase(name))
			.findAny()
			.orElse(NOT_ACCEPT);
	}
}
