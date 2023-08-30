package com.kuddy.apiserver.report.dto;

import com.kuddy.common.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResDto {
    private Long id;
    private Long writerId;
    private Long targetId;
    private String reason;
    private String explanation;
    private LocalDateTime createdDate;

    public static ReportResDto of(Report report){
        return ReportResDto.builder()
                .id(report.getId())
                .writerId(report.getWriter().getId())
                .targetId(report.getTargetId())
                .reason(report.getReason().getName())
                .explanation(report.getExplanation())
                .createdDate(report.getCreatedDate())
                .build();
    }
}
