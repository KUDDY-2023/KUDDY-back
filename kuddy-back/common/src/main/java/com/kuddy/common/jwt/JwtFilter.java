package com.kuddy.common.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kuddy.common.redis.RedisService;
import com.kuddy.common.security.exception.AuthExceptionHandler;
import com.kuddy.common.security.exception.ExpiredTokenException;
import com.kuddy.common.security.exception.InvalidRefreshTokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	private final RedisService redisService;
	private final AuthExceptionHandler authExceptionHandler;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		String jwt = resolveToken(request);

		try {
			if (StringUtils.hasText(jwt)) {
				if (jwtProvider.validateToken(response, jwt)) {
					Optional<String> isBlackList = redisService.getBlackList(jwt);
					isBlackList.ifPresent(t -> {
						throw new InvalidRefreshTokenException();
					});

					Authentication authentication = jwtProvider.getAuthentication(jwt);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					return;
				}
			}
		} catch (InvalidRefreshTokenException e) {
			log.error("Invalid refresh token exception:", e);
			authExceptionHandler.handleException(response, new ExpiredTokenException());
			return;
		}

		filterChain.doFilter(request, response);
	}

	// HTTP Request Header에서 Token 값 가져오기
	public String resolveToken(HttpServletRequest request) {
		// Header의 Authorization에 JWT이 담겨 올 것이다.
		String authorization = request.getHeader("Authorization");
		// Authorization에 들어있는 문자열이 공백이 아니고 Bearer로 시작하는지 검증한다.
		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
			// Token 값만 추출한다.
			return authorization.substring(7);
		}
		return null;
	}
}
