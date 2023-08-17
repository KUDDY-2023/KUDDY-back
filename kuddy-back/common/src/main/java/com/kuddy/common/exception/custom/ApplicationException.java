package com.kuddy.common.exception.custom;

import org.springframework.http.HttpStatus;

import com.kuddy.common.exception.type.ExceptionType;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException { //에러 메시지 상세화를 위해

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	public ApplicationException(HttpStatus httpStatus) {
		ExceptionType exceptionType = ExceptionType.from(this.getClass());
		this.httpStatus = httpStatus;
		this.errorCode = exceptionType.getErrorCode();
		this.message = exceptionType.getMessage();
	}

	public ApplicationException(HttpStatus httpStatus, String optionalMessage) {
		ExceptionType exceptionType = ExceptionType.from(this.getClass());
		this.httpStatus = httpStatus;
		this.errorCode = exceptionType.getErrorCode();
		this.message = exceptionType.getMessage() + optionalMessage;
	}
}