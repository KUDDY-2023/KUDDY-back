package com.kuddy.apiserver.spot.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotSearchReqDto {
    private String keyword;
    private String category;
    private List<String> district;
}
