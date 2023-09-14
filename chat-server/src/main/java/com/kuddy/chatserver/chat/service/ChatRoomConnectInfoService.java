package com.kuddy.chatserver.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.chat.exception.ChatRoomNotFoundException;
import com.kuddy.common.chat.exception.RoomNotFoundException;
import com.kuddy.common.chat.repository.RoomRepository;
import com.kuddy.common.redis.RedisService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomConnectInfoService {
	private final RoomRepository roomRepository;
	private final RedisService redisService;
	private static final String CHAT_ROOM_PREFIX = "Chatroom";

	public void connectChatRoom(Long chatRoomId, String email) {
		String key = buildRedisKey(chatRoomId, email);
		redisService.setData(key, Long.toString(chatRoomId));
	}

	public void disconnectChatRoom(Long chatRoomId, String email) {
		String key = buildRedisKey(chatRoomId, email);
		boolean isDeleted = redisService.deleteDataIfValueMatches(key, Long.toString(chatRoomId));
		if (!isDeleted) {
			throw new ChatRoomNotFoundException();
		}
	}

	private String buildRedisKey(Long chatRoomId, String email) {
		return CHAT_ROOM_PREFIX + chatRoomId + ":" + email;
	}
	public boolean isAllConnected(Long chatRoomId) {
		Room room = findRoomById(chatRoomId);
		String cerateMemberKey = buildRedisKey(room.getId(), room.getCreateMember().getEmail());
		String updateMemberKey = buildRedisKey(room.getId(), room.getJoinMember().getEmail());
		String roomId = Long.toString(room.getId());
		boolean isCreateMemberConnected = isMemberConnected(cerateMemberKey, roomId);
		boolean isJoinMemberConnected = isMemberConnected(updateMemberKey, roomId);
		return isCreateMemberConnected && isJoinMemberConnected;
	}
	public boolean isConnected(Long chatRoomId) {
		Room room = findRoomById(chatRoomId);
		String cerateMemberKey = buildRedisKey(room.getId(), room.getCreateMember().getEmail());
		String updateMemberKey = buildRedisKey(room.getId(), room.getJoinMember().getEmail());
		String roomId = Long.toString(room.getId());
		boolean isCreateMemberConnected = isMemberConnected(cerateMemberKey, roomId);
		boolean isJoinMemberConnected = isMemberConnected(updateMemberKey, roomId);

		return isCreateMemberConnected ^ isJoinMemberConnected;  // 둘 중 하나만 true일 경우 true 반환
	}
	@Transactional(readOnly = true)
	public Room findRoomById(Long chatRoomId) {
		return roomRepository.findById(chatRoomId).orElseThrow(RoomNotFoundException::new);
	}


	private boolean isMemberConnected(String key, String roomIdString) {
		return redisService.existsMember(key, roomIdString);
	}

}
