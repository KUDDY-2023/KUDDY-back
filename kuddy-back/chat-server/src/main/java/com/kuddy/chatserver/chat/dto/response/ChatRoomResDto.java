package com.kuddy.chatserver.chat.dto.response;

import com.kuddy.common.chat.domain.Room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResDto {
	private Long roomId;
	public ChatRoomResDto(Room room) {
		this.roomId = room.getId();
	}
}
