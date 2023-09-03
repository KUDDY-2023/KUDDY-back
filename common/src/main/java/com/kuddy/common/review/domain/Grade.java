package com.kuddy.common.review.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    PERFECT("0", "Perfect"),
    GOOD("1", "Good"),
    DISAPPOINT("2", "Disappoint");

    private final String code;
    private final String name;
}
