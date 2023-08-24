package com.kuddy.apiserver.spot.dto;

import com.kuddy.common.spot.domain.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotDetailResDto {
    private Long id;
    private String name;
    private Long contentId;
    private String district;
    private String category;

    private String about;
    private String phoneNum;
    private String homepage;
    private String location;
    private String post;
    private List<SpotResDto> nearbyPlace;
    private List<String> imageList;

    //찜한 멤버들

    public static SpotDetailResDto of(Spot spot, String about, String phoneNum, String homepage, String location, String post, List<SpotResDto> nearbyPlace, List<String> imageList){
        return SpotDetailResDto.builder()
                .id(spot.getId())
                .name(spot.getName())
                .contentId(spot.getContentId())
                .district(spot.getDistrict().getArea())
                .category(spot.getCategory().getType())
                .about(about)
                .phoneNum(phoneNum)
                .homepage(homepage)
                .location(location)
                .post(post)
                .nearbyPlace(nearbyPlace)
                .imageList(imageList)
                .build();
    }

}
