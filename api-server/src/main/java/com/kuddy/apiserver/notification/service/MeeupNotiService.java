package com.kuddy.apiserver.notification.service;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MeeupNotiService  {
    private final MeetupRepository meetupRepository;
    private final MailNotiService mailNotiService;

    public void pubishMeetupPayedEvent(String chatId) throws MessagingException {
        Meetup meetup = meetupRepository.findByChatId(chatId).orElseThrow(MeetupNotFoundException::new);
        Member kuddy = meetup.getKuddy();
        Member traveler = meetup.getTraveler();
        mailNotiService.sendMeetupPayedMail(kuddy.getEmail(), traveler.getEmail(), kuddy.getNickname(), traveler.getNickname());
    }

}
