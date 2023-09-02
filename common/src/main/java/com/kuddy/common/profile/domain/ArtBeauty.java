package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ArtBeauty {
	NOT_SELECTED("0", "Not Selected"),
	MUSIC("1", "Music"),
	PICTURE("2", "Picture"),
	FASHION("3", "Fashion"),
	PICTUREGI("4", "PictureGi"),
	MAKEUP("5", "Makeup"),
	BEAUTY("6", "Beauty"),
	VIEWING_EXHIBITION("7", "Viewing Exhibition"),
	DESIGN("8", "Design"),
	PILKA("9", "Pilka"),
	INTERIOR("10", "Interior"),
	POLAROID("11", "Polaroid");

	private final String code;
	private final String name;
}
