package com.kuddy.common.chat.repository;

import java.util.Optional;

import com.kuddy.common.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.chat.domain.Room;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {

	Optional<Room> findById(Long roomId);
	@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Room r WHERE r.id = :roomId AND (r.createMember = :member OR r.joinMember = :member)")
	boolean existsByRoomIdAndAnyMember(Long roomId, Member member);
}
