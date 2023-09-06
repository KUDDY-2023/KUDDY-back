package com.kuddy.apiserver.meetup.dto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.util.CustomDateUtil;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetupListResDto {
	private final List<SingleMeetup> meetupList;
	private final Integer totalMeetupCount;

	@Getter
	@AllArgsConstructor
	public static class SingleMeetup {
		private final Long meetupId;
		private final Long spotId;
		private final String spotName;
		private final String appointmentTime;
		private final String createdDate;
		private final boolean isReviewed;
		private final MeetupStatus meetupStatus;
		private final TargetMemberInfoDto targetMemberInfo;

		public SingleMeetup(Meetup meetup, Member loginMember) {
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

		public static SingleMeetup of(Meetup meetup, Member loginMember) {
			return new SingleMeetup(meetup, loginMember);
		}
	}

	public static MeetupListResDto from(List<Meetup> meetups, Member loginMember) {
		List<SingleMeetup> list = meetups.stream()
			.filter(meetup -> meetup != null)
			.map(meetup -> SingleMeetup.of(meetup, loginMember))
			.collect(Collectors.toList());

		return MeetupListResDto.builder()
			.meetupList(Collections.unmodifiableList(list))
			.totalMeetupCount(list.size())
			.build();
	}
}




