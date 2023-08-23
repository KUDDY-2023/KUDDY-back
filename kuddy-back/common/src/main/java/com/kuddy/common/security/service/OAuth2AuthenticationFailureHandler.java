package com.kuddy.common.security.service;


import static com.kuddy.common.security.repository.CookieAuthorizationRequestRepository.*;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.kuddy.common.security.repository.CookieAuthorizationRequestRepository;
import com.kuddy.common.util.CookieUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws
		IOException {
		String targetUrl = CookieUtils.getCookie(request, REDIRECT_URL_PARAM_COOKIE_KEY)
			.map(Cookie::getValue)
			.orElse(("/"));

		targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("token", "")
			.queryParam("error", exception.getLocalizedMessage())
			.build().toUriString();

		cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

}

