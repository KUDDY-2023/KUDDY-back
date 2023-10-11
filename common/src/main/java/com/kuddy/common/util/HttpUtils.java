package com.kuddy.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Slf4j
public class HttpUtils {
    public static <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, method, entity, responseType);
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            throw new RuntimeException("Failed to send HTTP request.");
        }
    }

    public static HttpEntity<?> createEntity(String accessToken) {

        return new HttpEntity<>(createHeaderWithAccessToken(accessToken));
    }

    public static HttpHeaders createHeaderWithAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    public static HttpHeaders createHeaderWithRefreshToken(String accessToken, String refreshToken){

        HttpHeaders headers = createHeaderWithAccessToken(accessToken);
        headers.add("RefreshToken", refreshToken);
        return headers;
    }

    public static String createRequestUrlToGetToken(String url, String grantType, String clientId, String redirectUri, String code) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("grant_type", grantType)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();
    }

    public static String createRequestUrlToGetTokenWithRefreshToken(String url, String grantType, String clientId, String refreshToken, String clientSecret) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("grant_type", grantType)
                .queryParam("client_id", clientId)
                .queryParam("refresh_token", refreshToken)
                .queryParam("client_secret", clientSecret)
                .toUriString();
    }

}