package com.kuddy.apiserver.review.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewReqDto {
    private Long meetupId;
    private String grade;
    private String content;
}
