package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Temperament {
	NOT_SELECTED("0", "Not Selected"),
	INTROVERT("1", "Introvert"),
	EXTROVERT("2", "Extrovert");

	private final String code;
	private final String name;
}
