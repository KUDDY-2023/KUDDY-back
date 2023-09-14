package com.kuddy.apiserver.spot.dto;

import com.kuddy.apiserver.member.dto.MemberResDto;
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
    private Long contentId;
    private String name;
    private String district;
    private String category;
    private Long heart;
    private String mapX;
    private String mapY;
    private String about;
    private String phoneNum;
    private String homepage;
    private String location;
    private String post;
    private Object additionalInfo;
    private List<String> imageList;
    private List<MemberResDto> kuddyList;
    private List<MemberResDto> travelerList;

    public static SpotDetailResDto of(Spot spot, String about, String phoneNum, String homepage, String location, String post, Object additionalInfo, List<String> imageList, List<MemberResDto> kuddyList, List<MemberResDto> travelerList){
        return SpotDetailResDto.builder()
                .name(spot.getName())
                .contentId(spot.getContentId())
                .district(spot.getDistrict().getArea())
                .category(spot.getCategory().getType())
                .heart(spot.getNumOfHearts())
                .mapX(spot.getMapX())
                .mapY(spot.getMapY())
                .about(about)
                .phoneNum(phoneNum)
                .homepage(homepage)
                .location(location)
                .post(post)
                .additionalInfo(additionalInfo)
                .imageList(imageList)
                .kuddyList(kuddyList)
                .travelerList(travelerList)
                .build();
    }

}
