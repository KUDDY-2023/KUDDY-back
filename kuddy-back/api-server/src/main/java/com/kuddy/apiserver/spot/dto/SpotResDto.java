package com.kuddy.apiserver.spot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpotResDto {
//    private Long id;
    private Long contentId;
    private String name;
    private String district;
    private String category;
    private String imageUrl;
    private String mapX;
    private String mapY;

    public static SpotResDto of(Spot spot){
        return SpotResDto.builder()
//                .id(spot.getId())
                .name(spot.getName())
                .contentId(spot.getContentId())
                .district(spot.getDistrict().getArea())
                .category(spot.getCategory().getType())
                .imageUrl(spot.getImageUrl())
                .build();
    }
}
