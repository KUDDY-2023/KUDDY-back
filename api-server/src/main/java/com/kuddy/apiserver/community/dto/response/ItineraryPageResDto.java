package com.kuddy.apiserver.community.dto.response;

import com.kuddy.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ItineraryPageResDto {
    private List<ItineraryResSimpleDto> posts;
    private PageInfo pageInfo;
}
