package com.kuddy.common.notification.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.notification.exception.KakaoCalendarAPIException;
import com.kuddy.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {
    private final RedisService redisService;
    private static final String TOKEN_CHECK_URI = "https://kapi.kakao.com/v1/user/access_token_info";
    private static final String TOKEN_REFRESH_URI = "https://kauth.kakao.com/oauth/token";

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENTID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    public boolean validateKakaoAccessToken(String kakaoAccessToken) {
        WebClient wc = WebClient.create(TOKEN_CHECK_URI);
        try{
            ResponseEntity<String> response = wc.get()
                    .header("Authorization", "Bearer " + kakaoAccessToken)
                    .retrieve()
                    // 4xx 클라이언트 오류 상태 코드 처리
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new KakaoCalendarAPIException()))
                    // 5xx 서버 오류 상태 코드 처리
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new KakaoCalendarAPIException()))
                    .toEntity(String.class).block();

            return (response != null ? response.getStatusCode() : null) == HttpStatus.OK;
        } catch (Exception e){
            log.error("Error while validating access token", e);
            return false;
        }



    }

    public Map<String, String> refreshKakaoTokens(String refreshToken) throws JsonProcessingException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", CLIENTID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("refresh_token", refreshToken);

        WebClient wc = WebClient.create(TOKEN_REFRESH_URI);
        ResponseEntity<String> response = wc.post()
                .uri(TOKEN_REFRESH_URI)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .toEntity(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        if (response != null) {
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {
            });
        }
        return null;
    }
}