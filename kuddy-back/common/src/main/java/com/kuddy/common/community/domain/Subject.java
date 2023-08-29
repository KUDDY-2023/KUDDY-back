package com.kuddy.common.community.domain;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Subject {
    FOOD(0, "food"),
    VIEW(1, "view"),
    KPOP(2, "k-pop");

    private final int value;
    private final String type;

    private Subject(int value, String type) {
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
