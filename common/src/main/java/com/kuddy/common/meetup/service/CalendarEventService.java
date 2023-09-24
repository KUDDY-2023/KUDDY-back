package com.kuddy.common.meetup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.service.GoogleCalendarService;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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

    public void deleteEvents(List<Calendar> eventList) throws JsonProcessingException {
        for(Calendar event : eventList){
            if (event.getMember().getProviderType().equals(ProviderType.KAKAO)) {
                kakaoCalendarService.deleteCalendarEvent(event);
            } else {
                googleCalendarService.deleteCalendarEvent(event);
            }
        }

    }
}
