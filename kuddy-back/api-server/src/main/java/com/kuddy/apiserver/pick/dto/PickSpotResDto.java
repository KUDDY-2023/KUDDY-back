package com.kuddy.apiserver.pick.dto;

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
public class PickSpotResDto {
    private Long contentId;
    private String name;
    private String  district;
    private String category;
    private String summary;
    private String detail;
    private List<String> pickImageList;

    public static PickSpotResDto of(PickSpot pickSpot){
        return PickSpotResDto.builder()
                .contentId(pickSpot.getSpot().getContentId())
                .name(pickSpot.getSpot().getName())
                .district(pickSpot.getSpot().getDistrict().getArea())
                .category(pickSpot.getSpot().getCategory().getType())
                .summary(pickSpot.getSummary())
                .detail(pickSpot.getDetail())
                .pickImageList(pickSpot.getPImageUrlList())
                .build();
    }
}
