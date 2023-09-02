package com.kuddy.apiserver.community.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUploadReqDto {
	private List<String> imgList;

}
