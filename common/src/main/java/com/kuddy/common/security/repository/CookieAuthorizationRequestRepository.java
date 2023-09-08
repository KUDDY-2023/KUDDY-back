package com.kuddy.common.security.repository;

import com.kuddy.common.util.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY = "oauth2AuthRequest";
    public static final String REDIRECT_URL_PARAM_COOKIE_KEY = "redirect_uri";
    public static final String GOOGLE_CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar.events";
    private static final int cookieExpireSeconds = 180;

    // 쿠키에 저장된 인증요청 정보 가져옴
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
        return oAuth2AuthorizationRequest;
    }

    // 주어진 OAuth2 인증 요청을 쿠키에 저장합니다. 만약 인증 요청이 null이라면, 현재 쿠키에 저장된 인증 요청을 삭제합니다.
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        Map<String, Object> additionalParameters = new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
        additionalParameters.put("access_type", "offline");
        additionalParameters.put("scope", GOOGLE_CALENDAR_SCOPE);
        additionalParameters.put("include_granted_scopes", true);
        additionalParameters.put("state", "state_parameter_passthrough_value");
        OAuth2AuthorizationRequest auth2AuthorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters)
                .build();
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY, CookieUtils.serialize(auth2AuthorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URL_PARAM_COOKIE_KEY);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URL_PARAM_COOKIE_KEY, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

	// 현재 HTTP request에서 OAuth2 인증 요청을 삭제하고, 삭제된 요청을 반환합니다.
	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		return this.loadAuthorizationRequest(request);
	}

	//현재 HTTP 요청과 응답에서 OAuth2 인증 요청과 관련된 쿠키를 모두 삭제합니다.
	public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_KEY);
		CookieUtils.deleteCookie(request, response, REDIRECT_URL_PARAM_COOKIE_KEY);
	}

}