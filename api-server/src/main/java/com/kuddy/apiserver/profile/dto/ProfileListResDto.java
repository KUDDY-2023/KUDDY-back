package com.kuddy.apiserver.profile.dto;

import java.util.List;

import com.kuddy.common.page.PageInfo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileListResDto {
	private List<ProfileThumbnailResDto> profileList;
	private PageInfo pageInfo;

	public ProfileListResDto(List<ProfileThumbnailResDto> profileList, PageInfo pageInfo) {
		this.profileList = profileList;
		this.pageInfo = pageInfo;
	}
}
