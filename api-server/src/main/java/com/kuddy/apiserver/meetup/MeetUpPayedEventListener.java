package com.kuddy.apiserver.meetup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.common.meetup.service.CalendarEventFacade;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetUpPayedEventListener {
    private final CalendarEventFacade calendarEventFacade;
    private final CalendarRepository calendarRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MeetupService.MeetupPayedEvent.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMeetupPayedEvent(MeetupService.MeetupPayedEvent event) throws IOException {
        Long kuddyId = event.getKuddyId();
        Long travelerId = event.getTravelerId();
        log.info("kuddy id  :  " + String.valueOf(kuddyId));
        log.info("traveler id  :  " + String.valueOf(travelerId));

        calendarEventFacade.createCalendarEvent(kuddyId, event.getMeetup(), event.getSpotName());
        calendarEventFacade.createCalendarEvent(travelerId, event.getMeetup(), event.getSpotName());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MeetupService.MeetupCanceledEvent.class)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleMeetupCanceledEvent(MeetupService.MeetupCanceledEvent event) throws JsonProcessingException {
        List<Calendar> eventList = calendarRepository.findAllByMeetup_Id(event.getMeetup().getId());
        calendarEventFacade.deleteEvents(eventList);
    }
}
