package com.kuddy.common.review.domain;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.meetup.domain.Meetup;
import com.kuddy.common.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetup_id", nullable = false)
    private Meetup meetup;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Grade grade;

    @Column(length = 500)
    private String content;

    @Builder
    public Review(Member writer, Meetup meetup, Grade grade, String content) {
        this.writer = writer;
        this.meetup = meetup;
        this.grade = grade;
        this.content = content;
    }
}
