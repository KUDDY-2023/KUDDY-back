package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Entertainment {
	NOT_SELECTED("0", "Not Selected"),
	MOVIES("1", "Movies"),
	NETFLIX("2", "Netflix"),
	KPOP("3", "Kpop"),
	ANIMATION("4", "Animation"),
	INSTAGRAM("5", "Instagram"),
	GAME("6", "Game"),
	YOUTUBE("7", "YouTube"),
	DRAMA("8", "Drama"),
	WEBTOON("9", "Webtoon"),
	SINGING("10", "Singing"),
	DISNEY("11", "Disney"),
	INSTRUMENTAL_MUSIC("12", "Instrumental Music"),
	EATING("13", "Eating"),
	HARRY_POTTER("14", "Harry Potter"),
	MARVEL("15", "Marvel"),
	LOL("16", "LOL"),
	NINTENDO("17", "Nintendo"),
	PLAYSTATION("18", "PlayStation"),
	STEAM("19", "STEAM"),
	PUBG("20", "PUBG"),
	PERFORMANCE("21", "Performance");

	private final String code;
	private final String name;
}

