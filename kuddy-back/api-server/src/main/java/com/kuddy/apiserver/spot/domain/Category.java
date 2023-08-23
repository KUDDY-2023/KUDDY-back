package com.kuddy.apiserver.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    Attraction,
    Culture,
    Shopping,
    Restaurant,
    Leisure,
    Festival;
}