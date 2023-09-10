package com.kuddy.apiserver.notification.controller;

import com.kuddy.apiserver.notification.service.CommentNotiService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final CommentNotiService notificationService;

    //알림 구독 (SSE)
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthUser Member member, @RequestHeader(value="Last-Event-ID",required = false,defaultValue = "") String lastEventId){
        return notificationService.subscribe(member.getId(), lastEventId);
    }

    // 전체 알림 읽음 처리
    @PostMapping("/read/list")
    public ResponseEntity<StatusResponse> readAllNotification(@AuthUser Member member){
        return notificationService.readAllNotification(member);
    }

    // 단일 알림 읽음 처리
    @PostMapping("/read/{notificationId}")
    public ResponseEntity<StatusResponse> readNotification(@PathVariable Long notificationId){
        return notificationService.readNotification(notificationId);
    }

    // 전체 알림 조회
    @GetMapping()
    public ResponseEntity<StatusResponse> getNotifications(@AuthUser Member member, @RequestParam int page, @RequestParam int size){
        return notificationService.findAllNotification(member.getId(), page, size);
    }

    //읽지 않은 알림 개수 조회
    @GetMapping("/count")
    public ResponseEntity<StatusResponse> countUnreadNotifications(@AuthUser Member member){
        return notificationService.countUnreadNotifications(member);
    }
}
