package com.kuddy.chatserver.chat.dto.response;

import java.time.ZoneId;

import com.kuddy.common.chat.domain.mongo.Chatting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResDto {
	private String id;
	private Long roomId;

	private String senderName;
	private String contentType;

	//동행 요청일 경우 범위 시작
	private String spotName;
	private Long spotContentId;
	private String appointmentTime; // 직렬화 매커니즘을 지원하지 않아서 String
	private String meetStatus;
	private String price; //
	//동행일 경우 범위끝
	private String content;
	private long sendDate;
	private long readCount;
	private boolean isMine;

	public ChatResDto(Chatting chat, String loginMemberNickname) {
		this.id = chat.getId();
		this.roomId = chat.getRoomId();
		this.senderName = chat.getSenderName();
		this.contentType = chat.getContentType();
		this.content = chat.getContent();
		this.sendDate = chat.getSendDate().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		this.readCount = chat.getReadCount();
		this.isMine = chat.getSenderName().equals(loginMemberNickname);
		this.spotContentId = chat.getSpotContentId();
		this.spotName = chat.getSpotName();
		this.price = chat.getPrice();
		this.appointmentTime = chat.getAppointmentTime();
		this.meetStatus = chat.getMeetStatus();
	}
}
