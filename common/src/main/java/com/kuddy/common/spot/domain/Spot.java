package com.kuddy.common.spot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "spot")
@NoArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private District district;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Long numOfHearts;

    @Builder
    public Spot(String name, Long contentId, District district, String imageUrl, Category category, Long numOfHearts) {
        this.name= name;
        this.contentId = contentId;
        this.district = district;
        this.category = category;
        this.imageUrl = imageUrl;
        this.numOfHearts = numOfHearts;
    }

    public void likeSpot() {
        numOfHearts++;
    }

    public void cancelSpot() {
        numOfHearts--;
    }
}