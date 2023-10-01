package com.kuddy.apiserver.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.apiserver.notification.service.MailNotiService;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import com.kuddy.common.notification.calendar.service.GoogleCalendarService;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification/test")
@RequiredArgsConstructor
public class NotiController {
    private final KakaoCalendarService kakaoCalendarService;
    private final MeetupRepository meetupRepository;
    private final CalendarRepository calendarRepository;
    private final GoogleCalendarService googleCalendarService;
    private final MailNotiService mailNotiService;
    private final MeetupService meetupService;
    private ApplicationEventPublisher eventPublisher;

//    @PostMapping("/{chatId}")
//    public void createEvent(@AuthUser Member member, @PathVariable String chatId) throws JsonProcessingException {
//        Meetup meetup = meetupRepository.findByChatId(chatId).get();
//        kakaoCalendarService.createKakaoEvent(member, meetup);
//    }
//
//    @DeleteMapping("/{chatId}")
//    public void deleteEvent(@AuthUser Member member, @PathVariable String chatId) throws JsonProcessingException {
//        Meetup meetup = meetupRepository.findByChatId(chatId).get();
//        List<Calendar> calendars = calendarRepository.findAllByMeetup_Id(meetup.getId());
//        log.info("number of events : " + String.valueOf(calendars.size()));
//        for(Calendar event : calendars){
//            member = event.getMember();
//            if(member.getProviderType().equals(ProviderType.KAKAO)){
//                kakaoCalendarService.deleteCalendarEvent(event);
//            }
//        }
//    }
//
//    @PostMapping("/google/{chatId}")
//    public void createGoogleEvent(@AuthUser Member member, @PathVariable String chatId) throws IOException {
//        Meetup meetup = meetupRepository.findByChatId(chatId).get();
//        googleCalendarService.createGoogleEvent(member, meetup);
//    }
//
//    @DeleteMapping("/google")
//    public void deleteGoogleEvent(@AuthUser Member member, @PathVariable String chatId) throws JsonProcessingException {
//        Meetup meetup = meetupRepository.findByChatId(chatId).get();
//        List<Calendar> calendars = calendarRepository.findAllByMeetup_Id(meetup.getId());
//        log.info("number of events : " + String.valueOf(calendars.size()));
//        for(Calendar event : calendars){
//            googleCalendarService.deleteCalendarEvent(event);
//        }
//    }
//
//    @PostMapping("/email")
//    public void sendMail() throws MessagingException {
//        mailNotiService.sendReviewRequestEmail();
//    }

//    @PostMapping("/calendars/{chatId}")
//    public String acceptMeetup(@PathVariable String chatId){
//        String newMeetupStatus = "PAYED";
//        meetupService.invokeCalendarEvent(chatId, newMeetupStatus);
//
//        return "일정 등록 완료";
//    }
//
//    @DeleteMapping("/calendars/{chatId}")
//    public String deleteMeetup(@PathVariable String chatId){
//        String newMeetupStatus = "KUDDY_CANCEL";
//        meetupService.invokeCalendarEvent(chatId, newMeetupStatus);
//        return "일정 삭제 완료";
//    }

}
