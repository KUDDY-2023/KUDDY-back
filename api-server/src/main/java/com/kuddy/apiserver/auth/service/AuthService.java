package com.kuddy.apiserver.auth.service;

import com.kuddy.common.security.user.KakaoUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.auth.dto.AccessTokenDto;
import com.kuddy.common.jwt.JwtProvider;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.redis.RedisService;
import com.kuddy.common.security.exception.AlreadyLogoutException;
import com.kuddy.common.security.exception.InvalidRefreshTokenException;
import com.kuddy.common.security.exception.InvalidTokenException;
import com.kuddy.common.security.exception.InvalidTokenTypeException;
import com.kuddy.common.security.exception.NotMatchStoredResfreshTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final JwtProvider jwtProvider;
	private final RedisService redisService;

	public void signOut(AccessTokenDto requestDto) {
		// accessToken에서 Authentication 추출하기
		String accessToken = requestDto.getAccessToken();
		Authentication authentication = jwtProvider.getAuthentication(accessToken);

		// Redis의 RefreshToken을 가져오면서, 이미 로그아웃된 사용자인 경우 예외 처리
		String refresh = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);

		// AccessToken의 남은 시간 추출 후 BlackList에 저장
		Long remainingTime = jwtProvider.getRemainingTime(accessToken);
		redisService.setData("BlackList:" + accessToken, "signOut", remainingTime);
		redisService.deleteData("RefreshToken:" + authentication.getName());
	}

	public AccessTokenDto refresh(String refreshToken) {
		// 들어온 refreshToekn 검증
		if (!jwtProvider.validateToken(refreshToken)) {
			log.error("유효하지 않은 토큰입니다. {}", refreshToken);
			throw new InvalidRefreshTokenException();
		}
		// refreshToken에서 Authentication 추출하기
		Authentication authentication = jwtProvider.getAuthentication(refreshToken);

		// Redis의 RefreshToken을 가져오면서, 로그아웃된 사용자인 경우 예외 처리
		String findRefreshToken = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);

		// 저장되어있던 refreshToken과 일치하는지 확인
		if (!refreshToken.equals(findRefreshToken)) {
			log.error("저장된 토큰과 일치하지 않습니다. {} {}", refreshToken, findRefreshToken);
			throw new NotMatchStoredResfreshTokenException();
		}

		// 토큰 생성을 위해 accessToken에서 Claims 추출
		String newAccessToken = jwtProvider.generateAccessToken(authentication.getName());

		return new AccessTokenDto(newAccessToken);
	}


	public UsernamePasswordAuthenticationToken getAuthenticationToken(String email, String password) {
		return new UsernamePasswordAuthenticationToken(email, password);
	}

	public ResponseCookie removeRefreshTokenCookie() {
		return ResponseCookie.from("refreshToken", null)
			.path("/") // 해당 경로 하위의 페이지에서만 쿠키 접근 허용. 모든 경로에서 접근 허용한다.
			.maxAge(0) // 쿠키의 expiration 타임을 0으로 하여 없앤다.
			.secure(true) // HTTPS로 통신할 때만 쿠키가 전송된다.
			.httpOnly(true) // JS를 통한 쿠키 접근을 막아, XSS 공격 등을 방어하기 위한 옵션이다.
			.build();
	}
	public void withdrawAuthInfo(Authentication authentication, String accessToken){
		// Redis의 RefreshToken을 가져오면서, 이미 로그아웃된 사용자인 경우 예외 처리
		String refresh = redisService.getRefreshToken(authentication.getName())
			.orElseThrow(AlreadyLogoutException::new);

		// AccessToken의 남은 시간 추출 후 BlackList에 저장
		Long remainingTime = jwtProvider.getRemainingTime(accessToken);
		redisService.setData("BlackList:" + accessToken, "signOut", remainingTime);
		redisService.deleteData("RefreshToken:" + authentication.getName());
	}
}
