package com.kuddy.apiserver.spot.domain;

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

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, name = "content_id")
    private Long contentId;

    @Column(nullable = false, length = 20)
    private String district;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Column
    private String imageUrl;

    @Builder
    public Spot(String name, Long contentId, String district, String imageUrl, Category category) {
        this.name= name;
        this.contentId = contentId;
        this.district = district;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}