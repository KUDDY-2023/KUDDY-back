package com.kuddy.chatserver.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.chat.domain.Room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRoomResDto {
	private Long roomId;
	private String message;

	public ChatRoomResDto(Room room) {
		if (isRoomValid(room)) {
			this.roomId = room.getId();
		} else {
			setErrorMessageForInvalidRoom();
		}
	}

	private boolean isRoomValid(Room room) {
		return room != null;
	}

	private void setErrorMessageForInvalidRoom() {
		this.message = "해당 유저와 생성한 채팅방이 존재하지 않습니다.";
	}

}
