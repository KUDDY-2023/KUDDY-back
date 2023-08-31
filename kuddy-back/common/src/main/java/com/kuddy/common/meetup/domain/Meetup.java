package com.kuddy.common.meetup.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.Spot;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Meetup extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meetup_id", updatable = false)
	private Long id;

	private Boolean isReviewed;
	private Boolean isPayed;
	private BigDecimal price;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private MeetupStatus meetupStatus;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kuddy_id")
	private Member kuddy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traveler_id")
	private Member traveler;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spot_id")
	private Spot spot;

	private LocalDateTime appointment;
	private String chatId;

	@Builder
	public Meetup(Boolean isReviewed, Boolean isPayed, BigDecimal price, MeetupStatus meetupStatus,
		Member kuddy,
		Member traveler, Spot spot, LocalDateTime appointment, String chatId) {
		this.isReviewed = false;
		this.isPayed = false;
		this.price = price;
		this.meetupStatus = meetupStatus;
		this.kuddy = kuddy;
		this.traveler = traveler;
		this.spot = spot;
		this.appointment = appointment;
		this.chatId =chatId;
	}


	public void updateReviewed(Boolean reviewed) {
		if(this.isReviewed.equals(isReviewed)){
			isReviewed = reviewed;
		}
	}

	public void updatePayed(Boolean payed) {
		if(this.isPayed.equals(payed)){
			isPayed = payed;
		}
	}

	public void updatePrice(BigDecimal price) {
		if (this.price != price) {
			this.price = price;
		}
	}

	public void updateMeetupStatus(String status) {
		MeetupStatus meetStatus = MeetupStatus.fromString(status);
		if (!Objects.equals(this.meetupStatus, meetStatus)) {
			this.meetupStatus = meetStatus;
		}
	}

	public void setKuddy(Member kuddy) {
		this.kuddy = kuddy;
	}

	public void setTraveler(Member traveler) {
		this.traveler = traveler;
	}

	public void updateSpot(Spot spot) {
		if (!Objects.equals(this.spot, spot)){
			this.spot = spot;
		}
	}

	public void updateAppointment(LocalDateTime appointment) {
		if(!this.appointment.equals(appointment)){
			this.appointment = appointment;
		}
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
}
