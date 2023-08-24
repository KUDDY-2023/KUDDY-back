package com.kuddy.common.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    Attraction("76"),
    Culture("78"),
    Shopping("79"),
    Restaurant("82"),
    Leisure("75"),
    Festival("85");

    private final String type;
}