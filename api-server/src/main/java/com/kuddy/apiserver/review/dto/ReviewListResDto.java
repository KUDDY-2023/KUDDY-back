package com.kuddy.apiserver.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewListResDto {
    private List<ReviewResDto> reviewResDto;
    private Long reviewCount;
    private Long meetupCount;
    private String  perfect;
    private String good;
    private String disappoint;


    public static ReviewListResDto of(List<ReviewResDto> reviewResDto, Long reviewCount, Long meetupCount){
        return ReviewListResDto.builder()
                .reviewResDto(reviewResDto)
                .reviewCount(reviewCount)
                .meetupCount(meetupCount)
                .build();
    }
}
