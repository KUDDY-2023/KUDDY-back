package com.kuddy.apiserver.pick.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.pick.domain.PickSpot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PickSpotResDto {
    private Long contentId;
    private String name;
    private String imageUrl;
    private String  district;
    private String category;
    private String summary;
    private String detail;
    private List<String> pickImageList;

    public static PickSpotResDto of(PickSpot pickSpot){
        return PickSpotResDto.builder()
                .contentId(pickSpot.getSpot().getContentId())
                .name(pickSpot.getSpot().getName())
                .imageUrl(pickSpot.getSpot().getImageUrl())
                .district(pickSpot.getSpot().getDistrict().getArea())
                .category(pickSpot.getSpot().getCategory().getType())
                .detail(pickSpot.getDetail())
                .pickImageList(pickSpot.getPImageUrlList())
                .build();
    }

    public PickSpotResDto(PickSpot pickspot) {
        this.contentId = pickspot.getSpot().getContentId();
        this.name = pickspot.getSpot().getName();
        this.imageUrl = pickspot.getSpot().getImageUrl();
        this.summary = pickspot.getSummary();
    }
}
