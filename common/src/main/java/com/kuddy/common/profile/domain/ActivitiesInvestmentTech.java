package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivitiesInvestmentTech {
	NOT_SELECTED("0", "Not Selected"),
	BUSINESS("1", "Business"),
	STOCK("2", "Stock"),
	INVESTMENT("3", "Investment"),
	REAL_ESTATE("4", "Real Estate"),
	BITCOIN("5", "Bitcoin");
	private final String code;
	private final String name;
}
