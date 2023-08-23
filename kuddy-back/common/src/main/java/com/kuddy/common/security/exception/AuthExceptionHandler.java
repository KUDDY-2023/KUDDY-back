package com.kuddy.common.security.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.exception.custom.UnAuthorizedException;
import com.kuddy.common.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthExceptionHandler {
	public void handleException(HttpServletResponse response,
		UnAuthorizedException ex) throws IOException {
		if (ex instanceof InvalidTokenTypeException) {
			commence(response, ex);
		} else if (ex instanceof InvalidTokenException) {
			commence(response, ex);
		}
		else if (ex instanceof ExpiredTokenException) {
			commence(response, ex);
		}
	}
	public void commence(HttpServletResponse response, UnAuthorizedException authException) throws
		IOException {

		log.error("Unauthorized Error : {}", authException.getMessage());
		// ErrorResponse 객체 생성
		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(HttpStatus.UNAUTHORIZED)
			.code(authException.getErrorCode())
			.message(authException.getMessage())
			.build();

		// JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);

		// 응답 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(jsonResponse);
	}
}
