package com.kuddy.common.notification.calendar.domain;

import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id", updatable = false)
    private Long id;

    private String eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetup_id")
    private Meetup meetup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Calendar(String eventId, Meetup meetup, Member member) {
        this.eventId = eventId;
        this.meetup = meetup;
        this.member = member;
    }
}
