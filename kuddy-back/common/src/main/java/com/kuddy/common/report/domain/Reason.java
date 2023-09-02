package com.kuddy.common.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {
    IMPERSONATION(0, "Impersonation"),
    SPAM(1, "Spam"),
    INTELLECTUAL_PROPERTY_VIOLATION(2, "Intellectual property violation"),
    HARASSMENT_OR_PRIVACY_VIOLATION(3, "Harassment or privacy violation"),
    NUDITY_OR_PORNOGRAPHY(4, "Nudity or pornography"),
    OTHER(5, "Other");

    private final int code;
    private final String name;
}
