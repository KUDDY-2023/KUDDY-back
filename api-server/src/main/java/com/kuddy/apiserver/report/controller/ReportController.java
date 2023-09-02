package com.kuddy.apiserver.report.controller;

import com.kuddy.apiserver.report.dto.ReportReqDto;
import com.kuddy.apiserver.report.service.ReportService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<StatusResponse> submitReport(@AuthUser Member member, @RequestBody ReportReqDto reportReqDto) {
        return reportService.createReport(member, reportReqDto);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<StatusResponse> getReport(@PathVariable("reportId") Long reportId) {
        return reportService.findReport(reportId);
    }

    @GetMapping
    public ResponseEntity<StatusResponse> getReportList() {
        return reportService.findReportList();
    }
}
