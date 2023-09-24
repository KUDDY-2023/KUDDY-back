package com.kuddy.apiserver.profile.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileSearchReqDto {

	private String genderType;
	private String areaName;
	private String languageType;
	private String interestGroup;
	private String interestContent;
	private String nickname;
	private String role;

}
