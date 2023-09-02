package com.kuddy.common.meetup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.meetup.domain.Meetup;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {

	Optional<Meetup> findByChatId(String chatId);
}
