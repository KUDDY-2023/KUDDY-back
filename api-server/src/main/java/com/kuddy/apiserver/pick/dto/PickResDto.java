package com.kuddy.apiserver.pick.dto;

import com.kuddy.common.pick.domain.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PickResDto {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private List<PickSpotResDto> pickSpotList;

    public static PickResDto of(Pick pick, List<PickSpotResDto> pickSpotList){
        return PickResDto.builder()
                .id(pick.getId())
                .title(pick.getTitle())
                .content(pick.getContent())
                .thumbnail(pick.getThumbnail())
                .pickSpotList(pickSpotList)
                .build();
    }
}
