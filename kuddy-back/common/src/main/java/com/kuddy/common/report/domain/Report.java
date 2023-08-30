package com.kuddy.common.report.domain;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "report")
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Column(nullable = false)
    private Long targetId;

    @Column(length = 40, nullable = false)
    private Reason reason;

    @Column(length = 500)
    private String explanation;

    @Builder
    public Report(Member writer, Long targetId, Reason reason, String explanation) {
        this.writer = writer;
        this.targetId = targetId;
        this.reason = reason;
        this.explanation = explanation;
    }
}
