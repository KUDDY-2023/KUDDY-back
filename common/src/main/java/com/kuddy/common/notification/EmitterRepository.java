package com.kuddy.common.notification;

import com.kuddy.common.notification.comment.domain.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    void deleteById(String emitterId);
    Map<String,Object> findAllEventCacheStartWithMemberId(String receiverId);
    Map<String, SseEmitter> findAllEmittersStartWithMemberId(String receiverId);
    void saveEventCache(String emitterId, Object object);
}
