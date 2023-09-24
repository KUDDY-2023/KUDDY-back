package com.kuddy.chatserver.chat.dto.response;

import java.util.Optional;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.util.CustomDateUtil;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetupResDto {
	private Long meetupId;
	private Long spotId;
	private String spotName;
	private String appointmentTime;
	private String createdDate;
	private boolean isReviewed;
	private MeetupStatus meetupStatus;
	private TargetMemberInfoDto targetMemberInfo;

	public MeetupResDto(Meetup meetup, Member loginMember) {
		this.meetupId = meetup.getId();
		this.appointmentTime = CustomDateUtil.toStringFormat(meetup.getAppointment());
		this.meetupStatus = Optional.ofNullable(meetup.getMeetupStatus()).orElse(MeetupStatus.UNKNOWN);
		this.createdDate = CustomDateUtil.toStringFormat(meetup.getCreatedDate());
		this.isReviewed = meetup.getIsReviewed();

		// Null-check for spot
		if (meetup.getSpot() != null) {
			this.spotId = meetup.getSpot().getId();
			this.spotName = meetup.getSpot().getName();
		} else {
			this.spotId = null;
			this.spotName = null;
		}
		if (meetup.getKuddy().getId().equals(loginMember.getId())) {
			this.targetMemberInfo = new TargetMemberInfoDto(meetup.getTraveler());
		} else {
			this.targetMemberInfo = new TargetMemberInfoDto(meetup.getKuddy());
		}
	}

	public static MeetupResDto of(Meetup meetup, Member loginMember) {
		return new MeetupResDto(meetup, loginMember);
	}
}
