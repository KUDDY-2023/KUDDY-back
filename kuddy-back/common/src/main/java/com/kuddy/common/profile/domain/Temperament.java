package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Temperament {
	INTROVERT("1", "EXTROVERT"),
	EXTROVERT("2", "Extrovert");

	private final String code;
	private final String name;
}
