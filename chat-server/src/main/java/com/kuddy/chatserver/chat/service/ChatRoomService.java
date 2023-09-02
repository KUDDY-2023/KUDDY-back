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
public class ChatRoomService {
	private final RoomRepository roomRepository;
	private final RedisService redisService;
	private static final String CHATROOMPREFIX = "Chatroom:";

	public void connectChatRoom(Long chatRoomId, String email) {
		String key = CHATROOMPREFIX + email;
		String roomIdString = String.valueOf(chatRoomId);
		redisService.setData(key,roomIdString);
	}


	public void disconnectChatRoom(Long chatRoomId, String email) {
		String key = CHATROOMPREFIX + email;
		String roomIdString = String.valueOf(chatRoomId);
		Boolean isDeleted = redisService.deleteDataIfValueMatches(key, roomIdString);
		if(isDeleted.equals(false)){
			throw new ChatRoomNotFoundException();
		}
	}
	@Transactional(readOnly = true)
	public boolean isAllConnected(Long chatRoomId) {
		Room room = roomRepository.findById(chatRoomId).orElseThrow(RoomNotFoundException::new);
		String roomIdToString = String.valueOf(chatRoomId);

		boolean isCreateMemberConnected = redisService.existsMember(room.getCreateMember().getEmail(), roomIdToString);
		boolean isJoinMemberConnected = redisService.existsMember(room.getJoinMember().getEmail(), roomIdToString);

		return isCreateMemberConnected && isJoinMemberConnected;
	}
	@Transactional(readOnly = true)
	public boolean isConnected(Long chatRoomId) {
		Room room = roomRepository.findById(chatRoomId).orElseThrow(RoomNotFoundException::new);
		String roomIdString = String.valueOf(chatRoomId);

		boolean isCreateMemberConnected = redisService.existsMember(room.getCreateMember().getEmail(), roomIdString);
		boolean isJoinMemberConnected = redisService.existsMember(room.getJoinMember().getEmail(), roomIdString);

		return isCreateMemberConnected ^ isJoinMemberConnected;  // 둘 중 하나만 true일 경우 true 반환
	}

}
