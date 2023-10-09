package com.kuddy.apiserver.notification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.apiserver.notification.service.CommentNotiService;
import com.kuddy.common.meetup.service.MeetupService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final CommentNotiService notificationService;
    private final MeetupService meetupService;
    private final KakaoCalendarService kakaoCalendarService;

    //알림 구독 (SSE)
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthUser Member member, @RequestHeader(value="Last-Event-ID",required = false,defaultValue = "") String lastEventId){
        return notificationService.subscribe(member.getId(), lastEventId);
    }

    // 전체 알림 읽음 처리
    @PostMapping("/read/list")
    public ResponseEntity<StatusResponse> readAllNotification(@AuthUser Member member){
        return notificationService.readAllCommentNotification(member);
    }

    // 단일 알림 읽음 처리
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<StatusResponse> readNotification(@PathVariable Long notificationId){
        return notificationService.readNotification(notificationId);
    }

    // 전체 알림 조회
    @GetMapping()
    public ResponseEntity<StatusResponse> getNotifications(@AuthUser Member member, @RequestParam int page, @RequestParam int size){
        return notificationService.findAllCommentNotification(member.getId(), page, size);
    }

    //읽지 않은 알림 개수 조회
    @GetMapping("/count")
    public ResponseEntity<StatusResponse> countUnreadNotifications(@AuthUser Member member){
        return notificationService.countUnreadCommentNotifications(member);
    }

    // 톡캘린더 일정 생성
    @PostMapping("/calendars/{meetupId}")
    public ResponseEntity<StatusResponse> createCalendarEvent(@AuthUser Member member, @PathVariable Long meetupId) throws JsonProcessingException {
        String newMeetupStatus = "PAYED";
        meetupService.invokeCalendarEvent(meetupId, member);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data("일정 등록 완료")
                .build());
    }

}
