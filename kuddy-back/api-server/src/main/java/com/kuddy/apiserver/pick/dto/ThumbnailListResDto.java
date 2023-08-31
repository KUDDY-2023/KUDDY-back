package com.kuddy.apiserver.pick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThumbnailListResDto {
    private List<ThumbnailResDto> thumbnailList;
    private int count;
}
