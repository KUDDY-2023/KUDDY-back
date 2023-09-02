package com.kuddy.apiserver.pick.dto;

import com.kuddy.common.pick.domain.Pick;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThumbnailResDto {
    private Long id;
    private String title;
    private String thumbnail;

    public static ThumbnailResDto of(Pick pick){
        return ThumbnailResDto.builder()
                .id(pick.getId())
                .title(pick.getTitle())
                .thumbnail(pick.getThumbnail())
                .build();
    }
}
