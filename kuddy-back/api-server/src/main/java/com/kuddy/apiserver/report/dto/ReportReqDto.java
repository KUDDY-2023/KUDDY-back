package com.kuddy.apiserver.report.dto;

import com.kuddy.common.report.domain.Reason;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportReqDto {
    private Long targetId;
    private Reason reason;
    private String explanation;
}
