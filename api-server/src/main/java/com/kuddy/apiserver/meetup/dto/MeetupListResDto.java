package com.kuddy.apiserver.meetup.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.meetup.domain.MeetupStatus;
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
	private List<SingleMeetup> meetupList;
	private Integer totalMeetupCount;

	@Getter
	public static class SingleMeetup{
		private Long meetupId;
		private Long spotId;
		private String spotName;
		private String appointmentTime;
		private String createdDate;
		private boolean isReviewed;
		private MeetupStatus meetupStatus;
		private TargetMemberInfoDto targetMemberInfo;

		public SingleMeetup(Meetup meetup){
			this.meetupId = meetup.getId();
			this.appointmentTime = CustomDateUtil.toStringFormat(meetup.getAppointment());
			this.meetupStatus = meetup.getMeetupStatus();
			this.createdDate = CustomDateUtil.toStringFormat(meetup.getCreatedDate());
			this.isReviewed = meetup.getIsReviewed();
			this.spotId = meetup.getSpot().getId();
			this.spotName = meetup.getSpot().getName();
		}

		public static MeetupListResDto.SingleMeetup of(Meetup meetup){
			return new MeetupListResDto.SingleMeetup(meetup);
		}
	}
	public static MeetupListResDto of(List<Meetup> meetups){
		return MeetupListResDto.builder()
			.meetupList(meetups.stream().map(SingleMeetup::of).collect(Collectors.toList()))
			.totalMeetupCount(meetups.size())
			.build();
	}

}
