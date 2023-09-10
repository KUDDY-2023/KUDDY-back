package com.kuddy.apiserver.notification.service;


import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MailNotiService {
    private final JavaMailSender javaMailSender;
    private final MeetupRepository meetupRepository;
    private final SpringTemplateEngine templateEngine;


    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Seoul")
    public void sendReviewRequestEmail() throws MessagingException {
        LocalDateTime targetDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        List<Meetup> meetups = meetupRepository.findByAppointment(targetDate);
        log.info("the number of today's meetups: "+ String.valueOf(meetups.size()));

        for(Meetup meetup : meetups) {
            Member kuddy = meetup.getKuddy();
            Member traveler = meetup.getTraveler();
            sendEmail(kuddy, traveler);
            sendEmail(traveler, kuddy);
        }
    }
    private void sendEmail(Member receiver, Member partner) throws MessagingException {
        String emailSubject = "[KUDDY] How was the meet up yesterday? Please write a review.";

        MimeMessage kuddyMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper kuddyMessageHelper = new MimeMessageHelper(kuddyMessage, false, "UTF-8");

        kuddyMessageHelper.setTo(receiver.getEmail());
        kuddyMessageHelper.setSubject(emailSubject);
        kuddyMessageHelper.setText(setContext(receiver.getNickname(), partner.getNickname()), true);
        javaMailSender.send(kuddyMessage);
    }

    public String setContext(String receiver, String partner){
        Context context = new Context();
        context.setVariable("receiver", receiver);
        context.setVariable("partner", partner);
        return templateEngine.process("email-form.html",context);
    }



}
