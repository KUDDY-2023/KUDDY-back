package com.kuddy.common.meetup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.service.GoogleCalendarService;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CalendarEventFacade {
    private final KakaoCalendarService kakaoCalendarService;
    private final GoogleCalendarService googleCalendarService;
    private final MemberRepository memberRepository;

    public void createCalendarEvent(Long memberId, Meetup meetup, String spotName) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        log.info("member 소셜 :" + member.getProviderType());
        if (member.getProviderType().equals(ProviderType.KAKAO)) {
            kakaoCalendarService.createKakaoEvent(member, meetup, spotName);
        } else {
            googleCalendarService.createGoogleEvent(member, meetup, spotName);
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
