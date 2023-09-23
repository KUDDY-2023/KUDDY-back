package com.kuddy.apiserver.meetup;

import com.kuddy.apiserver.notification.service.MailNotiService;
import com.kuddy.common.meetup.service.CalendarEventService;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MeetUpPayedEventListener {
    private final CalendarEventService calendarEventService;
    private final MemberRepository memberRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MeetupService.MeetupPayedEvent.class)
    public void handle(MeetupService.MeetupPayedEvent event) throws IOException {
        Member kuddy = event.getMeetup().getKuddy();
        Member traveler = event.getMeetup().getTraveler();
        calendarEventService.createCalendarEvent(kuddy, event.getMeetup());
        calendarEventService.createCalendarEvent(traveler, event.getMeetup());
    }
}
