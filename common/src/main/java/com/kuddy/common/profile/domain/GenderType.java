package com.kuddy.common.profile.domain;



public enum GenderType {
	MR, MS, NEUTRAL;

    public static GenderType fromString(String genderString) {
        if (genderString == null || genderString.trim().isEmpty()) {
            return null;
        }

        try {
            return GenderType.valueOf(genderString.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
