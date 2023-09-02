package com.kuddy.chatserver.chat.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ChatReqDto {

	@NotNull
	private String createMemberNickname;

	public ChatReqDto(String createMemberNickname) {
		this.createMemberNickname = createMemberNickname;
	}
}
