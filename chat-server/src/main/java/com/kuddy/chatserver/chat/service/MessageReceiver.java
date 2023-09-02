package com.kuddy.chatserver.chat.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.util.ConstantUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageReceiver {
	private final SimpMessageSendingOperations template;

	@KafkaListener(topics = ConstantUtil.KAFKA_TOPIC, containerFactory = "kafkaListenerContainerFactory")
	public void receiveMessage(Message message) {
		if (message.getIsUpdated() == 1) {
			log.info("전송 위치 = /topic/updates/"+ message.getRoomId());
			template.convertAndSend("/topic/updates/" + message.getRoomId(), message);
		} else {
			log.info("전송 위치 = /topic/group/"+ message.getRoomId());
			template.convertAndSend("/topic/group/" + message.getRoomId(), message);
		}
	}
}
