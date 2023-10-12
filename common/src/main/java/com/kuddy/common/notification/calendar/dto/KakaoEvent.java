package com.kuddy.common.notification.calendar.dto;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
@Builder
public class KakaoEvent {
    private String title;
    private Time time;
    private Location location;
    private int[] reminders;

    @Builder
    @Getter
    public static class Location {
        private String name;
        private String latitude;
        private String longitude;
    }

    @Getter
    @Builder
    public static class Time {
        private String start_at;
        private String end_at;
        private String time_zone;
        private boolean all_day;
        private boolean lunar;

    }

    @Builder
    public KakaoEvent(String title, Time time, Location location, int[] reminders) {
        this.title = title;
        this.time = time;
        this.location = location;
        this.reminders = reminders;
    }

    public static KakaoEvent from(Meetup meetup, Spot spot) {
        LocalDateTime dateTime = meetup.getAppointment();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();
        int remainder = minute % 5; // The minimum unit of start_at is 5 minutes
        LocalDateTime adjustedDateTime = dateTime.minusMinutes(remainder).minusSeconds(second);
        ZoneId koreaZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime zonedDateTime = adjustedDateTime.atZone(koreaZone);
        ZonedDateTime endOfTheDay = zonedDateTime
                .plusDays(1)
                .truncatedTo(ChronoUnit.DAYS);

        String startIsoString = zonedDateTime
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);
        String endIsoString = endOfTheDay
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        Time time = Time.builder()
                .start_at(startIsoString)
                .end_at(endIsoString)
                .time_zone("Asia/Seoul")
                .all_day(false)
                .lunar(false)
                .build();


        Location location = Location.builder()
                .name(spot.getName())
                .latitude(spot.getMapX())
                .longitude(spot.getMapY())
                .build();

        int[] reminders = {1440};  // 동행 하루 전 알림
        return KakaoEvent.builder()
                .title("Meet up")
                .location(location)
                .time(time)
                .reminders(reminders)
                .build();
    }
}