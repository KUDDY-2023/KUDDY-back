package com.kuddy.common.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Food {
	RESTAURANT("1", "Restaurant"),
	COFFEE("2", "Coffee"),
	DESSERT("3", "Dessert"),
	SUSHI("4", "Sushi"),
	ALCOHOL("5", "Alcohol"),
	CHICKEN("6", "Chicken"),
	TTEOKBOKKI("7", "Tteokbokki"),
	BEER("8", "Beer"),
	PASTA("9", "Pasta"),
	BREAD("10", "Bread"),
	HAMBURGER("11", "Hamburger"),
	SALAD("12", "Salad"),
	WINE("13", "Wine"),
	WHISKEY("14", "Whiskey");
	private final String code;
	private final String name;
}
