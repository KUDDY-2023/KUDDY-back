package com.kuddy.common.pick.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "pick")
@NoArgsConstructor
public class Pick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private String thumbnail;

    @OneToMany(mappedBy = "pick", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickSpot> pickSpotList = new ArrayList<>();

    @Builder
    public Pick(String title, String content, String thumbnail) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }
}
