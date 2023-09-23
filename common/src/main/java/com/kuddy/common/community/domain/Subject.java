package com.kuddy.common.community.domain;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Subject {
    FOOD(0, "food"),
    NATURE(1, "nature"),
    KPOP(2, "k-pop"),
    ATTRACTION(3, "attraction"),
    ENTERTAINMENT(4, "entertainment"),
    SHOPPING(5, "shopping"),
    LEISURE_SPORTS(6, "leisure sports"),
    HEALTH_SAFETY(7, "health and safety"),
    CONVENIENCE_FACILITIES(8, "convenience facilities"),
    PUBLIC_TRANSPORTATION(9, "public transportation"),
    TRAVEL_TIPS(10, "travel tips");

    private final int value;
    private final String type;

    Subject(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    private static final Map<String, String> SUBJECT_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(Subject::getType, Subject::name)));

    public static Subject of(final String type) {
        return Subject.valueOf(SUBJECT_MAP.get(type));
    }
}
