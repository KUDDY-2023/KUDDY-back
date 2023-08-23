package com.kuddy.common.exception.dto;


import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
	private HttpStatus status;
	private String errorCode;
	private String message;

	@Builder
	public ErrorResponse(HttpStatus status, String code, String message) {
		this.status = status;
		this.errorCode = code;
		this.message = message;
	}


}
