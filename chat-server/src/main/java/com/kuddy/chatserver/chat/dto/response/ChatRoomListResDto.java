package com.kuddy.chatserver.chat.dto.response;

import java.time.LocalDateTime;
import java.time.ZoneId;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomListResDto {
	private Long chatRoomId;

	private String createMember;

	private String joinMember;


	private long regDate;
	private Participant participant;
	private LatestMessage latestMessage;

	private Long unReadCount;

	public void setUnReadCount(Long unReadCount) {
		this.unReadCount = unReadCount;
	}

	@Builder
	public ChatRoomListResDto(Long chatRoomId, String createMember, String joinMember, LocalDateTime regDate,
		Participant participant) {
		this.chatRoomId = chatRoomId;
		this.createMember = createMember;
		this.joinMember = joinMember;
		this.regDate = regDate.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		this.participant= participant;
	}

	public void setLatestMessage(LatestMessage latestMessage) {
		this.latestMessage = latestMessage;
	}

	@Getter
	@AllArgsConstructor
	@ToString
	public static class Participant {
		private String nickname;
		private String profile;
	}

	@Getter
	@ToString
	public static class LatestMessage {
		private String context;
		private long sendAt;

		@Builder
		public LatestMessage(String context, LocalDateTime sendAt) {
			this.context = context;
			this.sendAt = sendAt.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
		}
	}
}
