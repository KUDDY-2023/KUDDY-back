package com.kuddy.common.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    ATTRACTION("76", "Attraction"),
    CULTURE("78", "Culture"),
    SHOPPING("79", "Shopping"),
    RESTAURANT("82", "Restaurant"),
    LEISURE("75", "Leisure"),
    FESTIVAL("85", "Festival");

    private final String code;
    private final String type;
}