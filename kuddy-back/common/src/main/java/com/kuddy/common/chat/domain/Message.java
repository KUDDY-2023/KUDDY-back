package com.kuddy.common.chat.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

import com.kuddy.common.chat.domain.mongo.Chatting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable { //Kafka와 Stomp Client 설정
	//Kafka에서 메시지 전달에 사용할 도메인 모델
	private String id;

	@NotNull
	private Long roomId;

	@NotNull
	private String contentType;

	@NotNull
	private String content;

	//동행 요청일 경우 범위 시작
	private String spotName;
	private Long spotContentId;
	private String appointmentTime; // 직렬화 매커니즘을 지원하지 않아서 String
	private String meetStatus;
	private String price; //
	//동행일 경우 범위끝

	private String senderName;
	private Long sendTime;
	private int readCount;
	@NotNull
	private String senderEmail;
	private int isUpdated;

	public void setSendTimeAndSender(LocalDateTime sendTime,String senderName, Integer readCount) {
		this.senderName = senderName;
		this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
		this.readCount = readCount;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Chatting convertEntity() {
		return Chatting.builder()
			.senderName(senderName)
			.roomId(roomId)
			.contentType(contentType)
			.content(content)
			.sendDate(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime())
			.readCount(readCount)
			.appointmentTime(appointmentTime)
			.meetStatus(meetStatus)
			.price(price)
			.spotName(spotName)
			.spotContentId(spotContentId)

			.build();
	}
}
