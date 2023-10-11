package com.kuddy.common.chat.domain.mongo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "chatting")
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Chatting { //MongoDB에서 메시지 저장에 사용할 도메인 모델
	@Id
	private String id;
	private Long roomId;
	private String senderName;
	private Long senderId;
	private String contentType;
	private String content;
	private LocalDateTime sendTime;
	private long readCount;

	//동행 요청일 경우 범위 시작
	private String spotName;
	private Long spotContentId;
	private String appointmentTime; // 직렬화 매커니즘을 지원하지 않아서 String
	private String meetStatus;
	private String price;
	//동행일 경우 범위끝

	public void updateSenderName(String newNickname){
		if(!this.senderName.equals(newNickname)){
			this.senderName = newNickname;
		}
	}


}
