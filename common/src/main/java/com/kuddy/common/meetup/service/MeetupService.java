package com.kuddy.common.meetup.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuddy.common.member.domain.ProviderType;
import com.kuddy.common.notification.calendar.domain.Calendar;
import com.kuddy.common.notification.calendar.repository.CalendarRepository;
import com.kuddy.common.notification.calendar.service.GoogleCalendarService;
import com.kuddy.common.notification.calendar.service.KakaoCalendarService;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.chat.domain.Message;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.meetup.repository.MeetupRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.repository.SpotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MeetupService implements ApplicationEventPublisherAware{
	private final MeetupRepository meetupRepository;
	private final SpotRepository spotRepository;
	private final KakaoCalendarService kakaoCalendarService;
	private final GoogleCalendarService googleCalendarService;
	private final CalendarRepository calendarRepository;
	private final MeetupQueryService meetupQueryService;
	private ApplicationEventPublisher eventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

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
	public void update(Message message) throws IOException {
		Meetup meetup = meetupRepository.findByChatId(message.getId()).orElseThrow(MeetupNotFoundException::new);
		Spot spot = findByContentId(message.getSpotContentId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime dateTime = LocalDateTime.parse(message.getAppointmentTime(), formatter);

		meetup.updateSpot(spot);
		meetup.updateAppointment(dateTime);
		meetup.updatePrice(new BigDecimal(message.getPrice()));
		
		boolean isMeetupStatusUpdated = !Objects.equals(message.getMeetStatus(), meetup.getMeetupStatus());
		meetup.updateMeetupStatus(message.getMeetStatus());

		if (isMeetupStatusUpdated) {
			MeetupStatus meetupStatus = MeetupStatus.fromString(message.getMeetStatus());
			switch (meetupStatus){
				case PAYED:
					eventPublisher.publishEvent(new MeetupPayedEvent(meetup));
					break;
				case KUDDY_CANCEL:
				case TRAVELER_CANCEL:
					eventPublisher.publishEvent(new MeetupCanceledEvent(meetup));
					break;
				default:
					break;
				}
		}
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

	@Transactional(readOnly = true)
	public List<Meetup> findListByMember(Member member) {
		RoleType roleType = member.getRoleType();
		List<MeetupStatus> excludedStatuses = Arrays.asList(MeetupStatus.REFUSED, MeetupStatus.ACCEPTED, MeetupStatus.NOT_ACCEPT);

		switch (roleType) {
			case KUDDY:
				return meetupQueryService.getKuddyMeetupList(member.getId(), excludedStatuses);
			case TRAVELER:
				return meetupQueryService.getTravelerMeetupList(member.getId(), excludedStatuses);
			default:
				throw new MeetupNotFoundException();
		}
	}

	public static class MeetupPayedEvent {
		@Getter
		private Meetup meetup;

		private MeetupPayedEvent(@NonNull Meetup meetup){
			this.meetup = meetup;
		}

	}

	public static class MeetupCanceledEvent{
		@Getter
		private Meetup meetup;

		private MeetupCanceledEvent(@NonNull Meetup meetup){
			this.meetup = meetup;
		}

	}

}
