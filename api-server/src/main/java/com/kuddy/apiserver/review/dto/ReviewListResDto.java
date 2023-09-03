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
    private Long count;
    private String  perfect;
    private String good;
    private String disappoint;


    public static ReviewListResDto of(List<ReviewResDto> reviewResDto, Long count){
        return ReviewListResDto.builder()
                .reviewResDto(reviewResDto)
                .count(count)
                .build();
    }
}
