package com.kuddy.apiserver.spot.controller;

import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.apiserver.spot.service.RecommendApiService;
import com.kuddy.apiserver.spot.service.SpotService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import com.kuddy.common.spot.domain.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spots/recommendation")
@RequiredArgsConstructor
public class SpotRecommendController {
    private final SpotService spotService;
    private final RecommendApiService recommendApiService;

    @GetMapping
    public ResponseEntity<StatusResponse> recommendSpot(@RequestParam(value = "page") int page, @RequestParam(value = "x") String mapX, @RequestParam(value = "y") String mapY) {
        Page<Spot> spotPage = spotService.getSpotsByDistance(page - 1, mapX, mapY);
        return spotService.changePageToResponse(spotPage, page);
    }
    @GetMapping("/recommendation/{contentId}")
    public ResponseEntity<StatusResponse> recommendFiveSpot(@PathVariable Long contentId, @RequestParam(value = "x") String mapX, @RequestParam(value = "y") String mapY) {
        return spotService.getFiveSpotsByDistance(contentId, mapX, mapY);
    }


    @GetMapping("/personal/{contentId}")
    public ResponseEntity<StatusResponse> recommendPersonalFiveSpot(@AuthUser Member member,
                                                            @PathVariable Long contentId) {
        List<SpotResDto> response = recommendApiService.getFiveSpotsByDistance(member, contentId);
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }
}
