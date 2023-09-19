package com.kuddy.common.notification.calendar.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.dto.GoogleEvent;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import com.kuddy.common.notification.exception.GoogleCalendarAPIException;
import com.kuddy.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCalendarService {
    private final GoogleAuthService googleAuthService;
    private final RedisService redisService;
    private final CalendarRepository calendarRepository;
    private static final String TOKEN_REFRESH_URI = "https://www.googleapis.com/oauth2/v4/token";
    private static final String GOOGLE_CALENDAR_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";
    private static final Long googleAccessTokenValidationMs = 1000 * 60 * 60L;
    private static final Long googleRefreshTokenValidationMs = 15552000000L;


    // 구글 access token 유효성 검사 및 재발급
    public String verifyAndRefreshGoogleToken(String email) throws JsonProcessingException {
        String googleAccessToken = redisService.getData("GoogleAccessToken:" + email);
        boolean isValidAccessToken = googleAuthService.validateGoogleAccessToken(googleAccessToken);
        if (!isValidAccessToken) {
            String googleRefreshToken = redisService.getData("GoogleRefreshToken" + email);
            Map<String, String> newTokens = googleAuthService.refreshGoogleTokens(googleRefreshToken);

            redisService.setData("GoogleAccessToken:" + email, newTokens.get("access_token"), googleAccessTokenValidationMs);
            redisService.setData("GoogleRefreshToken" + email, newTokens.get("refresh_token"), googleRefreshTokenValidationMs);

            return newTokens.get("access_token");
        } else {
            return googleAccessToken;
        }
    }

    public void createGoogleEvent(Member member, Meetup meetup) throws IOException {
        String googleAccessToken = verifyAndRefreshGoogleToken(member.getEmail());

        GoogleEvent googleEvent = GoogleEvent.from(meetup);
        ObjectMapper objectMapper = new ObjectMapper();
        String googleEventString = objectMapper.writeValueAsString(googleEvent);
        log.info(String.valueOf(BodyInserters.fromValue(googleEvent)));

        WebClient wc = WebClient.create(GOOGLE_CALENDAR_URL);
        ResponseEntity<String> response = wc.post()
                .header("Authorization", "Bearer "+ googleAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(googleEvent)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    // 4xx 클라이언트 오류 상태 코드 처리
                    return Mono.error(new GoogleCalendarAPIException());
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    // 5xx 서버 오류 상태 코드 처리
                    return Mono.error(new GoogleCalendarAPIException());
                })
                .toEntity(String.class).block();

        String eventId = null;

        if(response != null && response.getBody() != null) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            eventId = jsonNode.get("id").asText();
        }
        calendarRepository.save(com.kuddy.common.notification.calendar.domain.Calendar.builder()
                .eventId(eventId)
                .meetup(meetup)
                .member(member)
                .build());

        log.info("created event id : " + eventId);
    }

    public void deleteCalendarEvent(Calendar event) throws JsonProcessingException {
        String googleAccessToken = verifyAndRefreshGoogleToken(event.getMember().getEmail());
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(GOOGLE_CALENDAR_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE); //WebClient에 의한 Encoding 옵션 끄기
        WebClient wc = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(GOOGLE_CALENDAR_URL)
                .build();
        ResponseEntity<String> response = wc.delete()
                .uri(uriBuilder ->
                        uriBuilder.path("/{eventId}")
                                .build(event.getEventId()))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+ googleAccessToken)
                .retrieve()
                .toEntity(String.class).block();

        calendarRepository.delete(event);

    }
}
