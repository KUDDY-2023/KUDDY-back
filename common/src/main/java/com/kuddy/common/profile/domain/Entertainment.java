package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Entertainment {
	NOT_SELECTED("0", "NOT_SELECTED"),
	MOVIES("1", "MOVIES"),
	KPOP("2", "KPOP"),
	ANIMATION("3", "ANIMATION"),
	SOCIAL_MEDIA("4", "SOCIAL_MEDIA"),
	GAME("5", "GAME"),
	VIDEO_STREAMING("6", "VIDEO_STREAMING"),
	DRAMA("7", "DRAMA"),
	WEBTOON("8", "WEBTOON"),
	SINGING("9", "SINGING"),
	MUSIC("10", "MUSIC"),
	EATING("11", "EATING");

	private final String code;
	private final String name;
}

