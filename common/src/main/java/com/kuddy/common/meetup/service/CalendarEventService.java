package com.kuddy.common.meetup.service;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.notification.calendar.service.GoogleCalendarService;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CalendarEventService {
    private final KakaoCalendarService kakaoCalendarService;
    private final GoogleCalendarService googleCalendarService;

    public void createCalendarEvent(Member member, Meetup meetup) throws IOException {
        if (member.getProviderType().equals(ProviderType.KAKAO)) {
            kakaoCalendarService.createKakaoEvent(member, meetup);
        } else {
            googleCalendarService.createGoogleEvent(member, meetup);
        }
    }
}
