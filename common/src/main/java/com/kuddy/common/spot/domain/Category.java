package com.kuddy.common.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<String, String> CATEGORY_CODE_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Category::getCode, Category::name)));

    public static Category valueOfCode(final String code) {
        return Category.valueOf(CATEGORY_CODE_MAP.get(code));
    }

    public static boolean hasValue(String code) {
        return Arrays.stream(Category.values()).anyMatch(v -> v.code.equals(code));
    }
}