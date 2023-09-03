package com.kuddy.apiserver.review.dto;

import com.kuddy.apiserver.member.dto.MemberResDto;
import com.kuddy.common.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResDto {
    private Long id;
    private Long meetupId;
    private MemberResDto writerInfo;
    private MemberResDto kuddyInfo;
    private String grade;
    private String content;
    private LocalDateTime createdDate;

    public static ReviewResDto of(Review review){
        return ReviewResDto.builder()
                .id(review.getId())
                .meetupId(review.getMeetup().getId())
                .writerInfo(MemberResDto.of(review.getWriter()))
                .kuddyInfo(MemberResDto.of(review.getMeetup().getKuddy()))
                .grade(review.getGrade().getName())
                .content(review.getContent())
                .createdDate(review.getCreatedDate())
                .build();
    }
}
