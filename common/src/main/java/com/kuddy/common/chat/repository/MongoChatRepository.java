package com.kuddy.common.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.kuddy.common.chat.domain.mongo.Chatting;

public interface MongoChatRepository extends MongoRepository<Chatting, String> {


	List<Chatting> findByRoomId(Long roomId);
	Optional<Chatting> findById(String id);

	Page<Chatting> findByRoomIdOrderBySendTimeDesc(Long roomId, Pageable pageable);

	List<Chatting> findBySenderId(Long memberId);
}
