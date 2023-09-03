package com.kuddy.apiserver.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KuddyReviewListResDto {
    private List<ReviewResDto> reviewResDto;
    private Long count;
    private String  perfect;
    private String good;
    private String disappoint;
}
