package com.kuddy.common.chat.domain.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@RedisHash(value = "chatRoom")
public class ChatRoom {
	@Id
	private String key;

	private Long roomId;

	private String email;

	@Builder
	public ChatRoom(Long roomId, String email){
		this.key = String.valueOf(roomId) + email;
		this.roomId = roomId;
		this.email = email;
	}
}

