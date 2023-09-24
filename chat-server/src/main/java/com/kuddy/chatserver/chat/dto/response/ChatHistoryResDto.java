package com.kuddy.chatserver.chat.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class ChatHistoryResDto {
	private ReceiverInfoResDto receiverInfo;
	private List<ChatResDto> chatList;
}
