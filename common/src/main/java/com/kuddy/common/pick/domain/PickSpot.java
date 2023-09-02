package com.kuddy.common.pick.domain;

import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "pick_spot")
@NoArgsConstructor
public class PickSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_id", nullable = false)
    private Pick pick;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    private Spot spot;

    @Column(nullable = false, length = 100)
    private String summary;

    @Column(nullable = false, length = 500)
    private String detail;

    @OneToMany(mappedBy = "pickSpot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickImage> pickImageList = new ArrayList<>();

    @Builder
    public PickSpot(Pick pick, Spot spot, String summary, String detail) {
        this.pick = pick;
        this.spot = spot;
        this.summary = summary;
        this.detail = detail;
    }

    public List<String> getPImageUrlList() {
        List<String> imageUrlList = new ArrayList<>();
        for(PickImage pickImage : pickImageList) {
            imageUrlList.add(pickImage.getImageUrl());
        }
        return imageUrlList;
    }
}
