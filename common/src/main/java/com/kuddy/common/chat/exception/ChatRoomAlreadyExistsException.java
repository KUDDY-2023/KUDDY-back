package com.kuddy.common.chat.exception;

import com.kuddy.common.exception.custom.BadRequestException;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatRoomAlreadyExistsException extends BadRequestException {
	public ChatRoomAlreadyExistsException(String optionalMessage) {
		super(optionalMessage);
	}
}
