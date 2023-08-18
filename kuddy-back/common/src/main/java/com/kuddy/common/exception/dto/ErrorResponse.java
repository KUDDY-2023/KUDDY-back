package com.kuddy.common.exception.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
	private HttpStatus status;
	private String errorCode;
	private String message;
	private LocalDateTime date = LocalDateTime.now();

	@Builder
	public ErrorResponse(HttpStatus status, String code, String message) {
		this.status = status;
		this.errorCode = code;
		this.message = message;
	}


}
