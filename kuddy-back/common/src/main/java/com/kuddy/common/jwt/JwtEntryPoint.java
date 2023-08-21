package com.kuddy.common.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import net.minidev.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.exception.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint { //인증에 실패할 경우 진행될 EntryPoint를 생성

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

		log.error("Unauthorized Error : {}", authException.getMessage());
		// ErrorResponse 객체 생성
		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(HttpStatus.UNAUTHORIZED)
			.code("AUTH_ERROR")
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

