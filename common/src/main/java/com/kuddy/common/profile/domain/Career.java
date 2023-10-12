package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Career {
	NOT_SELECTED("0", "NOT_SELECTED"),
	EDUCATION("1", "EDUCATION"),
	IT("2", "IT"),
	PROGRAMMING("3", "PROGRAMMING"),
	MARKETING("4", "MARKETING"),
	STARTUP("5", "STARTUP"),
	INTERNSHIP("6", "INTERNSHIP"),
	HR("7", "HR");

	private final String code;
	private final String name;

	}
