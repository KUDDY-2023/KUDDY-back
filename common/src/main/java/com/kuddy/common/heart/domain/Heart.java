package com.kuddy.common.heart.domain;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "heart")
@NoArgsConstructor
public class Heart{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Builder
    public Heart(Member member, Spot spot) {
        this.member = member;
        this.spot = spot;
    }
}