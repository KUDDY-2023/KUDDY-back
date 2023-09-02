package com.kuddy.chatserver.chat.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kuddy.common.chat.domain.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSender {
	private final KafkaTemplate<String, Message> kafkaTemplate;

	// 메시지를 지정한 Kafka 토픽으로 전송
	public void send(String topic, Message data) {

		// KafkaTemplate을 사용하여 메시지를 지정된 토픽으로 전송
		log.info("send : " + data);
		kafkaTemplate.send(topic, data);
	}
}
