package com.kuddy.chatserver.notification.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kuddy.chatserver.notification.dto.NotificationResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.EmitterRepositoryImpl;
import com.kuddy.common.notification.comment.domain.Notification;
import com.kuddy.common.notification.comment.domain.Notificationtype.NotificationType;
import com.kuddy.common.notification.comment.repository.CommentNotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatNotiService {
	private final EmitterRepositoryImpl emitterRepository;
	private final CommentNotificationRepository notificationRepository;

	private void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object data) {
		try {
			sseEmitter.send(SseEmitter.event()
				.id(eventId)
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(emitterId);
		}
	}
	private Notification createNotification(Member receiver, NotificationType notificationType, String content, Long contentId){
		return Notification.builder()
			.receiver(receiver)
			.notificationType(notificationType)
			.content(content)
			.contentId(contentId)
			.isRead(true) //읽지 않음 상태로 초기 세팅
			.build();
	}
	@Async
	public void send(Member receiver, NotificationType notificationType, String content, Long contentId){
		Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, contentId));
		String receiverId = String.valueOf(receiver.getId());
		String eventId = receiverId + "_" + System.currentTimeMillis();
		Map<String, SseEmitter> emitters = emitterRepository.findAllEmittersStartWithMemberId(receiverId);
		emitters.forEach( (key, emitter) -> {
			emitterRepository.saveEventCache(key, notification);
			sendNotification(emitter, eventId, key, NotificationResDto.create(notification));
		});

	}

	public SseEmitter subscribe(Long memberId, String lastEventId) {
		String emitterId = makeUniqueEmitterId(memberId);
		Long timeout = 60L * 1000L * 60L; // 1시간
		SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(timeout));

		sseEmitter.onCompletion(()-> emitterRepository.deleteById(emitterId));
		sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

		String eventId = makeUniqueEmitterId(memberId);
		sendNotification(sseEmitter, eventId, emitterId, "EventStream Created. [memberId=" + memberId + "]");

		if (hasLostData(lastEventId)) {
			sendLostData(lastEventId, memberId, emitterId, sseEmitter);
		}
		return sseEmitter;
	}

	private boolean hasLostData(String lastEventId) {
		return !lastEventId.isEmpty();
	}

	private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
		Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithMemberId(String.valueOf(memberId));
		eventCaches.entrySet().stream()
				.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
				.forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
	}

	private String makeUniqueEmitterId(Long memberId){
		return memberId + "_" + System.currentTimeMillis();
	}
}
