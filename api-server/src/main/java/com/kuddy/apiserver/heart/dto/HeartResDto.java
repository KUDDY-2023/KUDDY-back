package com.kuddy.apiserver.heart.dto;

import com.kuddy.apiserver.spot.dto.SpotResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartResDto {
    private List<SpotResDto> spots;
    private Long memberId;
    private Long count;
}
