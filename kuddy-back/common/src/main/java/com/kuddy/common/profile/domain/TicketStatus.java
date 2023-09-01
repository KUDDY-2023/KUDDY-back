package com.kuddy.common.profile.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TicketStatus {
	NOT_SUBMITTED("0", "Not Submitted"),
	CERTIFICATION_COMPLETE("1", "Certification Complete"),
	UNDER_REVIEW("2", "Under Review"),
	PHOTO_UNRECOGNIZABLE("3", "Photo Unrecognizable"),
	INVALID_PHOTO("4", "Invalid Photo");

	private final String code;
	private final String description;
}
