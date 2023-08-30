package com.kuddy.apiserver.report.service;

import com.kuddy.apiserver.report.dto.ReportReqDto;
import com.kuddy.apiserver.report.dto.ReportResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.report.domain.Report;
import com.kuddy.common.report.repository.ReportRepository;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public ResponseEntity<StatusResponse> createReport(Member writer, ReportReqDto reportReqDto) {
        Report report = reportRepository.save(Report.builder()
                .writer(writer)
                .targetId(reportReqDto.getTargetId())
                .reason(reportReqDto.getReason())
                .explanation(reportReqDto.getExplanation())
                .build());
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.CREATED.getStatusCode())
                .message(StatusEnum.CREATED.getCode())
                .data(ReportResDto.of(report))
                .build());
    }
}
