package com.kuddy.common.notification.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.notification.calendar.dto.NewGoogleAccessTokenReqDto;
import com.kuddy.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {
    private final RedisService redisService;
    private static final String TOKEN_CHECK_URI = "https://www.googleapis.com/oauth2/v1/tokeninfo";
    private static final String REFRESH_TOKEN_URI = "https://www.googleapis.com/oauth2/v4/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENTID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret")
    private String CLIENT_SECRET;



    public boolean validateGoogleAccessToken(String googleAccessToken) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(TOKEN_CHECK_URI);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(TOKEN_CHECK_URI)
                .build();
        ResponseEntity<String> response = wc.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("access_token", URLEncoder.encode(googleAccessToken, StandardCharsets.UTF_8))
                        .build())
                .retrieve()
                .toEntity(String.class).block();

        return response.getStatusCode() == HttpStatus.OK;
    }

    public Map<String, String> refreshGoogleTokens(String refreshToken) throws JsonProcessingException {
        NewGoogleAccessTokenReqDto reqDto = new NewGoogleAccessTokenReqDto(CLIENTID, CLIENT_SECRET, refreshToken, "refresh_token");

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(REFRESH_TOKEN_URI);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(REFRESH_TOKEN_URI)
                .build();
        ResponseEntity<String> response = wc.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(reqDto))
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
