package com.kuddy.common.security.service;

import com.kuddy.common.jwt.JwtProvider;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.redis.RedisService;
import com.kuddy.common.security.repository.CookieAuthorizationRequestRepository;
import com.kuddy.common.security.user.GoogleUserInfo;
import com.kuddy.common.security.user.KakaoUserInfo;
import com.kuddy.common.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.kuddy.common.security.repository.CookieAuthorizationRequestRepository.REDIRECT_URL_PARAM_COOKIE_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private static final Long kakaoAccessTokenValidationMs = 1000 * 60 * 60 * 6L;
    private static final Long kakaoRefreshTokenValidationMs = 1000 * 60 * 60 * 24 * 30L;
    private static final Long googleAccessTokenValidationMs = 1000 * 60 * 60L;
    private static final Long googleRefreshTokenValidationMs = 15552000000L;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient auth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(),
                authToken.getName());

        String oauthAccessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
        log.info("oauth access token:" + oauthAccessToken);

        OAuth2RefreshToken refreshToken = auth2AuthorizedClient.getRefreshToken();
        log.info("oauth refresh token: " + refreshToken);

        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        String email = null;
        String oauthRefreshToken;
        if (providerType.equals(ProviderType.KAKAO)) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
            email = kakaoUserInfo.getEmail();
            log.info("kakao");
            oauthRefreshToken = refreshToken != null
                    ? refreshToken.getTokenValue()
                    : redisService.getData("KakaoRefreshToken:" + email);
            redisService.setData("KakaoAccessToken:" + email, oauthAccessToken);
            redisService.setData("KakaoRefreshToken:" + email, oauthRefreshToken);
        } else if (providerType.equals(ProviderType.GOOGLE)) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(attributes);
            email = googleUserInfo.getEmail();
            log.info("google");
            oauthRefreshToken = refreshToken != null
                    ? refreshToken.getTokenValue()
                    : redisService.getData("GoogleRefreshToken:" + email);
            redisService.setData("GoogleAccessToken:" + email, oauthAccessToken);
            redisService.setData("GoogleRefreshToken:" + email, oauthRefreshToken);
        } else {
            Map<String, Object> providerData = (Map<String, Object>) attributes.get(providerType.name().toLowerCase());
            if (providerData != null) {
                email = providerData.get("email").toString();
            } else {
                // Handle the case where providerData is null
            }
        }

        String targetUrl = determineTargetUrl(request, response, authentication);
        log.info("targetUrl = " + targetUrl);

        String url = makeRedirectUrl(email, targetUrl);

        ResponseCookie responseCookie = generateRefreshTokenCookie(email);
        response.setHeader("Set-Cookie", responseCookie.toString());
        response.getWriter().write(url);


        if (response.isCommitted()) {
            logger.info("응답이 이미 커밋된 상태입니다. " + url + "로 리다이렉트하도록 바꿀 수 없습니다.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, url);
    }


    private String makeRedirectUrl(String email, String redirectUrl) {

        if (redirectUrl.equals(getDefaultTargetUrl())) {
            redirectUrl = "http://localhost:3000";
        }
        log.info(redirectUrl);

        String accessToken = jwtProvider.generateAccessToken(email);
        log.info(accessToken);

        return UriComponentsBuilder.fromHttpUrl(redirectUrl)
                .path("/oauth2/redirect")
                .queryParam("accessToken", accessToken)
                .queryParam("redirectUrl", redirectUrl)
                .build()
                .encode()
                .toUriString();


    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUrl = CookieUtils.getCookie(request, REDIRECT_URL_PARAM_COOKIE_KEY).map(Cookie::getValue);
        String targetUrl = redirectUrl.orElse(getDefaultTargetUrl());
        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    public ResponseCookie generateRefreshTokenCookie(String email) {
        String refreshToken = jwtProvider.generateRefreshToken(email);
        Long refreshTokenValidationMs = jwtProvider.getRefreshTokenValidationMs();

        redisService.setData("RefreshToken:" + email, refreshToken, refreshTokenValidationMs);
		log.info("refresh:" + refreshToken);
		return ResponseCookie.from("refreshToken", refreshToken)
			.path("/") // 해당 경로 하위의 페이지에서만 쿠키 접근 허용. 모든 경로에서 접근 허용한다.
			.domain(".kuddy.co.kr")
			.maxAge(TimeUnit.MILLISECONDS.toSeconds(refreshTokenValidationMs)) // 쿠키 만료 시기(초). 없으면 브라우저 닫힐 때 제거
			.secure(true) // HTTPS로 통신할 때만 쿠키가 전송된다.
			.sameSite("None")
			.httpOnly(true) // JS를 통한 쿠키 접근을 막아, XSS 공격 등을 방어하기 위한 옵션이다.
			.build();
	}
}
