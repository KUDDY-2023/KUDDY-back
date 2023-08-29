package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DecisionMaking {
	JUDGING("1", "Judging"),
	PROSPECTING("2", "Prospecting");

	private final String code;
	private final String name;
}
