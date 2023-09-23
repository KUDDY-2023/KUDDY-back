package com.kuddy.apiserver.meetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.apiserver.notification.service.MailNotiService;
import com.kuddy.common.meetup.service.CalendarEventService;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MeetUpPayedEventListener {
    private final CalendarEventService calendarEventService;
    private final CalendarRepository calendarRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MeetupService.MeetupPayedEvent.class)
    public void handleMeetupPayedEvent(MeetupService.MeetupPayedEvent event) throws IOException {
        Member kuddy = event.getMeetup().getKuddy();
        Member traveler = event.getMeetup().getTraveler();
        calendarEventService.createCalendarEvent(kuddy, event.getMeetup());
        calendarEventService.createCalendarEvent(traveler, event.getMeetup());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MeetupService.MeetupCanceledEvent.class)
    public void handleMeetupCanceledEvent(MeetupService.MeetupCanceledEvent event) throws JsonProcessingException {
        List<Calendar> eventList = calendarRepository.findAllByMeetup_Id(event.getMeetup().getId());
        calendarEventService.deleteEvents(eventList);
    }
}
