package com.kuddy.common.meetup.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.repository.SpotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetupService {
	private final MeetupRepository meetupRepository;
	private final SpotRepository spotRepository;

	public void create(Message message, Member findMember, Member receiveMember){
		Spot spot = findByContentId(message.getSpotContentId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = LocalDateTime.parse(message.getAppointmentTime(), formatter);
		Meetup meetup = Meetup.builder()
			.kuddy(findMember)
			.traveler(receiveMember)
			.meetupStatus(MeetupStatus.NOT_ACCEPT)
			.appointment(dateTime)
			.spot(spot)
			.price(new BigDecimal(message.getPrice()))
			.chatId(message.getId())
			.build();
		meetupRepository.save(meetup);
		long totalMeetNum = countMeetupsForKuddy(findMember.getId());
		findMember.getProfile().updateKuddyLevelByMeetup(totalMeetNum,findMember.getRoleType());
	}
	public void update(Message message){
		Meetup meetup = meetupRepository.findByChatId(message.getId()).orElseThrow(MeetupNotFoundException::new);
		Spot spot = findByContentId(message.getSpotContentId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = LocalDateTime.parse(message.getAppointmentTime(), formatter);
		meetup.updateMeetupStatus(message.getMeetStatus());
		meetup.updateSpot(spot);
		meetup.updateAppointment(dateTime);
		meetup.updatePrice(new BigDecimal(message.getPrice()));

	}
	@Transactional(readOnly = true)
	public Spot findByContentId(Long contentId){
		return spotRepository.findByContentId(contentId);
	}

	@Transactional(readOnly = true)
	public Long countMeetupsForKuddy(Long kuddyId) {
		List<MeetupStatus> excludedStatuses = Arrays.asList(MeetupStatus.REFUSED, MeetupStatus.KUDDY_CANCEL, MeetupStatus.TRAVELER_CANCEL);
		return meetupRepository.countByKuddyIdAndMeetupStatusNotIn(kuddyId, excludedStatuses);
	}
}
