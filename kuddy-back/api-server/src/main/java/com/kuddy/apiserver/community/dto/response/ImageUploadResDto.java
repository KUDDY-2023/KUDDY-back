package com.kuddy.apiserver.community.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUploadResDto {
	private String rawFile;
	private String fileName;
	private String presignedUrl;

}
