package com.kuddy.common.chat.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.constraints.NotNull;

import com.kuddy.common.chat.domain.mongo.Chatting;
import com.kuddy.common.member.domain.Member;

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

	private Long senderId;
	private String senderName;
	private Long sendTime;
	private int readCount;
	@NotNull
	private String senderEmail;
	private int isUpdated;

	public void setSendTimeAndSender(LocalDateTime sendTime,String senderName, Long senderId,Integer readCount) {
		this.senderName = senderName;
		this.sendTime = sendTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		this.senderId = senderId;
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
			.senderId(senderId)
			.content(content)
			.spotName(spotName != null ? spotName : null)
			.spotContentId(spotContentId != null ? spotContentId : null)
			.appointmentTime(appointmentTime != null ? appointmentTime : null)
			.meetStatus(meetStatus != null ? meetStatus : null)
			.price(price != null ? price : null)
			.sendTime(Instant.ofEpochMilli(sendTime).atZone(ZoneId.of("UTC")).toLocalDateTime())
			.readCount(readCount)
			.build();
	}


	public static Message convertToMessage(Chatting chatting, Member sender) {
		LocalDateTime sendTime = chatting.getSendTime();
		long sendTimeMillis = sendTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();

		return Message.builder()
			.id(chatting.getId())
			.roomId(chatting.getRoomId())
			.contentType(chatting.getContentType())
			.content(chatting.getContent())
			.spotName(chatting.getSpotName())
			.spotContentId(chatting.getSpotContentId())
			.appointmentTime(chatting.getAppointmentTime())
			.meetStatus(chatting.getMeetStatus())
			.price(chatting.getPrice())
			.senderId(sender.getId())
			.senderName(sender.getNickname())
			.sendTime(sendTimeMillis)
			.readCount((int)chatting.getReadCount())
			.senderEmail(sender.getEmail())
			.isUpdated(1)
			.build();
	}
}
