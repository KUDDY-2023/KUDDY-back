package com.kuddy.apiserver.profile.dto.request;

import com.kuddy.common.profile.domain.GenderType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileSearchReqDto {

	private GenderType genderType;
	private String areaName;
	private String languageType;
	private String interestGroup;
	private String interestContent;

}
