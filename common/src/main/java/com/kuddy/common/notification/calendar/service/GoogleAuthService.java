package com.kuddy.common.notification.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.notification.calendar.dto.NewGoogleAccessTokenReqDto;
import com.kuddy.common.notification.exception.GoogleCalendarAPIException;
import com.kuddy.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {
    private final RedisService redisService;
    private static final String TOKEN_CHECK_URI = "https://www.googleapis.com/oauth2/v1/tokeninfo";
    private static final String REFRESH_TOKEN_URI = "https://www.googleapis.com/oauth2/v4/token";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENTID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    public boolean validateGoogleAccessToken(String googleAccessToken) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(TOKEN_CHECK_URI);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(TOKEN_CHECK_URI)
                .build();

        try{
            ResponseEntity<String> response = wc.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_token", URLEncoder.encode(googleAccessToken, StandardCharsets.UTF_8))
                            .build())
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (response != null) {
                HttpStatus httpStatus = response.getStatusCode();
                log.info("access token 검증 status code : " + httpStatus);
                return httpStatus.is2xxSuccessful();
            }else{
                return false;
            }
        } catch (Exception e){
            log.error("Error while validating access token", e);
            return false;
        }

    }

    public Map<String, String> refreshGoogleTokens(String refreshToken) throws JsonProcessingException {
        NewGoogleAccessTokenReqDto reqDto = new NewGoogleAccessTokenReqDto(CLIENTID, CLIENT_SECRET, refreshToken, "refresh_token");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(reqDto);

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(REFRESH_TOKEN_URI);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(REFRESH_TOKEN_URI)
                .build();
        ResponseEntity<String> response = wc.post()
                .body(BodyInserters.fromValue(jsonString))
                .retrieve()
                .toEntity(String.class).block();

        if (response != null) {
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {
            });
        }
        return null;
    }
}
