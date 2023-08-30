package com.kuddy.common.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Reason {
    IMPERSONATION(1, "Impersonation"),
    SPAM(2, "Spam"),
    INTELLECTUAL_PROPERTY_VIOLATION(3, "Intellectual property violation"),
    HARASSMENT_OR_PRIVACY_VIOLATION(4, "Harassment or privacy violation"),
    NUDITY_OR_PORNOGRAPHY(5, "Nudity or pornography"),
    OTHER(6, "Other");

    private final int code;
    private final String name;
}
