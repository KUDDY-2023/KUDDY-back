package com.kuddy.common.notification.calendar.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.calendar.dto.KakaoEvent;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import com.kuddy.common.notification.exception.KakaoCalendarAPIException;
import com.kuddy.common.redis.RedisService;
import com.kuddy.common.spot.domain.Spot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoCalendarService {
    private final KakaoAuthService kakaoAuthService;
    private final RedisService redisService;
    private final CalendarRepository calendarRepository;
    private static final String KAKAO_CALENDAR_URL = "https://kapi.kakao.com/v2/api/calendar";
    private static final Long kakaoAccessTokenValidationMs = 1000 * 60 * 60 * 6L;
    private static final long kakaoRefreshTokenValidationMs = 1000L * 60L * 60L * 24L * 30L * 2L;

    // 카카오 access token 유효성 검사 및 재발급
    public String verifyAndRefreshKakaoToken(String email) throws JsonProcessingException {
        String kakaoAccessToken = redisService.getData("KakaoAccessToken:" + email);
//        boolean isValidAccessToken = kakaoAuthService.validateKakaoAccessToken(kakaoAccessToken);
        boolean isValidAccessToken = false;
        if (!isValidAccessToken) {
            String kakaoRefreshToken = redisService.getData("KakaoRefreshToken:" + email);
            Map<String, String> newTokens = kakaoAuthService.refreshKakaoTokens(kakaoRefreshToken);
            redisService.setData("KakaoAccessToken:" + email, newTokens.get("access_token"));
            // refresh_token 값은 유효기간이 1개월 미만인 경우에만 갱신되어 담겨옴
            if( newTokens.containsKey("refresh_token")){
                redisService.setData("KakaoRefreshToken:" + email, newTokens.get("refresh_token"));
            }

            return newTokens.get("access_token");
        } else {
            return kakaoAccessToken;
        }

    }

    // 카카오 일반 일정 생성
    public void createKakaoEvent(Member member, Meetup meetup, String spotName) throws JsonProcessingException {
        String kakaoAccessToken = verifyAndRefreshKakaoToken(member.getEmail());
        ObjectMapper objectMapper = new ObjectMapper();
        String kakaoEventString = objectMapper.writeValueAsString(KakaoEvent.from(meetup, spotName));

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(KAKAO_CALENDAR_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(KAKAO_CALENDAR_URL)
                .build();

        ResponseEntity<String> response = wc.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/create/event")
                        .queryParam("calendar_id", "primary")
                        .queryParam("event", URLEncoder.encode(kakaoEventString, StandardCharsets.UTF_8)).build())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+ kakaoAccessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    // 4xx 클라이언트 오류 상태 코드 처리
                    return Mono.error(new KakaoCalendarAPIException());
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    // 5xx 서버 오류 상태 코드 처리
                    return Mono.error(new KakaoCalendarAPIException());
                })
                .toEntity(String.class).block();

        String eventId = null;
        if(response != null && response.getBody() != null) {
            Map<String, String> responseData = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});
            eventId = responseData.get("event_id");
        }
        calendarRepository.save(Calendar.builder()
                .eventId(eventId)
                .meetup(meetup)
                .member(member)
                .build());

        log.info("created event id : " + eventId);
    }

    // 카카오 일반 일정 삭제
    public void deleteCalendarEvent(Calendar event) throws JsonProcessingException {
        String kakaoAccessToken = verifyAndRefreshKakaoToken(event.getMember().getEmail());
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(KAKAO_CALENDAR_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(KAKAO_CALENDAR_URL)
                .build();
        ResponseEntity<String> response = wc.delete()
                .uri(uriBuilder ->
                        uriBuilder.path("/delete/event")
                                .queryParam("event_id", event.getEventId())
                                .build())
                .header("Authorization", "Bearer "+ kakaoAccessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    // 4xx 클라이언트 오류 상태 코드 처리
                    return Mono.error(new KakaoCalendarAPIException());
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    // 5xx 서버 오류 상태 코드 처리
                    return Mono.error(new KakaoCalendarAPIException());
                })
                .toEntity(String.class).block();

        calendarRepository.delete(event);
    }
}
