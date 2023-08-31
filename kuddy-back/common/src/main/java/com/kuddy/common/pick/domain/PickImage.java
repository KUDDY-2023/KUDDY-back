package com.kuddy.common.pick.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "pick_image")
@NoArgsConstructor
public class PickImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_spot_id", nullable = false)
    private PickSpot pickSpot;

    @Column(nullable = false)
    private String imageUrl;

    @Builder
    public PickImage(PickSpot pickSpot, String imageUrl) {
        this.pickSpot = pickSpot;
        this.imageUrl = imageUrl;
    }
}
