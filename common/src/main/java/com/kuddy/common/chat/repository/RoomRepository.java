package com.kuddy.common.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.chat.domain.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

	Optional<Room> findById(Long roomId);
}
