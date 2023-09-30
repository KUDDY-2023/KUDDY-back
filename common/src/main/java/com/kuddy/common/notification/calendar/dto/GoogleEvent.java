package com.kuddy.common.notification.calendar.dto;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class GoogleEvent {
    private EventDateTimeDto start;
    private EventDateTimeDto end;
    private String location;
    private String summary;
    private EventRemindersDto reminders;

    @Getter
    @Builder
    public static class EventDateTimeDto {
        private String dateTime;
        private String timeZone;
    }

    @Getter
    @Builder
    public static class EventRemindersDto {
        private boolean useDefault;
        private List<EventReminderOverrideDTO> overrides;

    }
    @Getter
    @Builder
    public static class EventReminderOverrideDTO {
        private String method;
        private int minutes;

    }

    @Builder
    public GoogleEvent(EventDateTimeDto start, EventDateTimeDto end, String location, String summary, EventRemindersDto reminders) {
        this.start = start;
        this.end = end;
        this.location = location;
        this.summary = summary;
        this.reminders = reminders;
    }

    public static GoogleEvent from(Meetup meetup, String spotName){
        LocalDateTime dateTime = meetup.getAppointment();
        ZoneId koreaZone = ZoneId.of("Asia/Seoul");

        int minute = dateTime.getMinute();
        int remainder = minute % 5; // The minimum unit of start_at is 5 minutes
        LocalDateTime adjustedDateTime = dateTime.minusMinutes(remainder);
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

        EventDateTimeDto start = EventDateTimeDto.builder()
                .dateTime(startIsoString)
                .timeZone("Asia/Seoul")
                .build();
        EventDateTimeDto end = EventDateTimeDto.builder()
                .dateTime(endIsoString)
                .timeZone("Asia/Seoul")
                .build();

        EventRemindersDto reminders = EventRemindersDto.builder()
                .useDefault(false)
                .overrides(Collections.singletonList(EventReminderOverrideDTO.builder()
                        .method("email")
                        .minutes(1440)
                        .build()))
                .build();

        return GoogleEvent.builder()
                .end(end)
                .start(start)
                .location(spotName)
                .summary("Meet up")
                .reminders(reminders)
                .build();

    }

}



