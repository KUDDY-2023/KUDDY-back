package com.kuddy.common.community.domain;

public enum PostType {
    ITINERARY(0, "itinerary"),
    JOIN_US(1, "joinus"),
    OTHERS(2, "others");

    private final int value;
    private final String type;

    private PostType(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
