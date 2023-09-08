package com.kuddy.common.meetup.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kuddy.common.meetup.domain.QMeetup.*;


import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetupQueryService {
	private final JPAQueryFactory jpaQueryFactory;

	@Transactional(readOnly = true)
	public List<Meetup> getKuddyMeetupList(Long memberId, List<MeetupStatus> excludedStatuses) {
		return jpaQueryFactory
			.selectFrom(meetup)
			.where(meetup.kuddy.id.eq(memberId).and(meetup.meetupStatus.notIn(excludedStatuses)))
			.orderBy(meetup.createdDate.desc())
			.fetch();
	}
	@Transactional(readOnly = true)
	public List<Meetup> getTravelerMeetupList(Long memberId, List<MeetupStatus> excludedStatuses) {
		return jpaQueryFactory
			.selectFrom(meetup)
			.where(meetup.traveler.id.eq(memberId).and(meetup.meetupStatus.notIn(excludedStatuses)))
			.orderBy(meetup.createdDate.desc())
			.fetch();
	}





}
