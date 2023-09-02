package com.kuddy.common.report.exception;

import com.kuddy.common.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReportNotFoundException extends NotFoundException {
    public ReportNotFoundException(Long id) {
        super("id=" + id);
    }
}