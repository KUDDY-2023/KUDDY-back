package com.kuddy.apiserver.spot.dto;

import com.kuddy.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotPageResDto {
    private List<SpotResDto> spots;
    private PageInfo pageInfo;
}
