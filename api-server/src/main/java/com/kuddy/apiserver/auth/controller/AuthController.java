package com.kuddy.apiserver.auth.controller;

import static org.springframework.http.HttpHeaders.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.apiserver.auth.dto.AccessTokenDto;
import com.kuddy.apiserver.auth.service.AuthService;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	private static final String LOGOUT_SUCCESS = "성공적으로 로그아웃 되었습니다.";

	@PostMapping("/blacklist")
	public ResponseEntity<StatusResponse> signOut(@Valid @RequestBody final AccessTokenDto requestDto) {
		authService.signOut(requestDto);
		ResponseCookie responseCookie = authService.removeRefreshTokenCookie();

		return ResponseEntity.ok()
			.header(SET_COOKIE, responseCookie.toString())
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(LOGOUT_SUCCESS)
				.build());

	}

	@PostMapping("/refresh")
	public ResponseEntity<StatusResponse> refresh(@CookieValue(value = "refreshToken", required = false) Cookie rtCookie) {

		String refreshToken = rtCookie.getValue();

		AccessTokenDto resDto = authService.refresh(refreshToken);

		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(resDto)
			.build());
	}

}
