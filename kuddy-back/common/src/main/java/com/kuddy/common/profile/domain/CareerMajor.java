package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CareerMajor {
	NOT_SELECTED("0", "Not Selected"),
	STUDY("1", "Study"),
	WORK_LIFE("2", "Work life"),
	LANGUAGE("3", "Language"),
	EDUCATION("4", "Education"),
	DESIGN("5", "Design"),
	IT("6", "IT"),
	JOB("7", "Job"),
	PART_TIME("8", "Part-time"),
	MEDICINE("9", "Medicine"),
	PROGRAMMING("10", "Programming"),
	CHANGING("11", "Changing"),
	MARKETING("12", "Marketing"),
	STARTUP("13", "Startup"),
	INTERN("14", "Intern"),
	HR("15", "HR");

	private final String code;
	private final String name;

	}
