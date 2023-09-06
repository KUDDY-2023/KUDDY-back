package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.page.PageInfo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileListResDto {
	private List<ProfileResDto> profileList;
	private PageInfo pageInfo;

	public ProfileListResDto(List<ProfileResDto> profileList, PageInfo pageInfo) {
		this.profileList = profileList;
		this.pageInfo = pageInfo;
	}
}
