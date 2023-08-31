package com.kuddy.common.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.kuddy.common.chat.domain.mongo.Chatting;

public interface MongoChatRepository extends MongoRepository<Chatting, String> {

	List<Chatting> findByRoomId(Long roomId);

	Page<Chatting> findByRoomIdOrderBySendDateDesc(Long roomId, Pageable pageable);
}
