package com.kuddy.common.notification.calendar.repository;

import com.kuddy.common.notification.calendar.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByMeetup_Id(Long meetupId);
}
