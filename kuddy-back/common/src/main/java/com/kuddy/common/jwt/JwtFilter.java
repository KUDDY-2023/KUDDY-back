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
import com.kuddy.common.security.exception.InvalidRefreshTokenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	private final RedisService redisService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// HTTP Request Header에서 Token 값 가져오기
		String jwt = resolveToken(request);

		// 유효성 검사 후, 정상적인 토큰인 경우 Security Context에 저장한다.
		if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {

			// BlackList에 존재하는 토큰으로 요청이 온 경우.
			Optional<String> isBlackList = redisService.getBlackList(jwt);
			isBlackList.ifPresent(t -> {
				throw new InvalidRefreshTokenException();
			});

			Authentication authentication = jwtProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);
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
