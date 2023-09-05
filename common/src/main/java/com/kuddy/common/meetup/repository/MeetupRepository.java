package com.kuddy.common.meetup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;


public interface MeetupRepository extends JpaRepository<Meetup, Long> {

	Optional<Meetup> findByChatId(String chatId);
	@Query("SELECT COUNT(m) FROM Meetup m WHERE m.kuddy.id = :kuddyId AND m.meetupStatus NOT IN :excludedStatuses")
	Long countByKuddyIdAndMeetupStatusNotIn(@Param("kuddyId") Long kuddyId, @Param("excludedStatuses") List<MeetupStatus> excludedStatuses);

	@Query("SELECT m FROM Meetup m WHERE m.kuddy.id = :kuddyId AND m.meetupStatus NOT IN :excludedStatuses ORDER BY m.appointment DESC")
	List<Meetup> findAllByKuddyAndMeetupStatusNotInOrderByDesc(@Param("kuddyId") Long kuddyId, @Param("excludedStatus") MeetupStatus excludedStatuses);

	@Query("SELECT m FROM Meetup m WHERE m.traveler.id = :travelerId AND m.meetupStatus NOT IN :excludedStatus ORDER BY m.appointment DESC")
	List<Meetup> findAllByTravelerAndMeetupStatusNotOrderByDesc(@Param("travelerId") Long travelerId, @Param("excludedStatus") MeetupStatus excludedStatus);


}
