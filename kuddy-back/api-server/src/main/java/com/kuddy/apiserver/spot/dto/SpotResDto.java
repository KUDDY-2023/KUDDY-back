package com.kuddy.apiserver.spot.dto;

import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotResDto {
    private Long id;
    private String name;
    private Long contentId;
    private String district;
    private String category;
    private String imageUrl;

    public static SpotResDto of(Spot spot){
        return SpotResDto.builder()
                .id(spot.getId())
                .name(spot.getName())
                .contentId(spot.getContentId())
                .district(spot.getDistrict().getArea())
                .category(spot.getCategory().getType())
                .imageUrl(spot.getImageUrl())
                .build();
    }
}
