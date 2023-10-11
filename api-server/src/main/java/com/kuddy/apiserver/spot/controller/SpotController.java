package com.kuddy.apiserver.spot.controller;

import com.kuddy.apiserver.spot.dto.SpotSearchReqDto;
import com.kuddy.apiserver.spot.service.TourApiService;
import com.kuddy.apiserver.spot.service.SpotService;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final TourApiService tourApiService;

    @PostMapping("/sync")
    public ResponseEntity<StatusResponse> syncTourData(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "date") String date) {
        spotService.synchronizeTourData(tourApiService.getSyncList(page, size, date));
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<StatusResponse> getSpotsByCategory(@PathVariable String category, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Spot> spotPage = spotService.findSpotByCategory(category.toUpperCase(), page - 1, size);
        return spotService.changePageToResponse(spotPage, page);
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<StatusResponse> getSpotsByDistrict(@PathVariable String district, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Spot> spotPage = spotService.findSpotByDistrict(district.toUpperCase(), page - 1, size);
        return spotService.changePageToResponse(spotPage, page);
    }

    @GetMapping("")
    public ResponseEntity<StatusResponse> getSpotsList(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Spot> spotPage = spotService.findAllSpots(page - 1, size);
        return spotService.changePageToResponse(spotPage, page);
    }

    @GetMapping("/recommendation")
    public ResponseEntity<StatusResponse> recommendSpot(@RequestParam(value = "page") int page, @RequestParam(value = "x") String mapX, @RequestParam(value = "y") String mapY) {
        Page<Spot> spotPage = spotService.getSpotsByDistance(page - 1, mapX, mapY);
        return spotService.changePageToResponse(spotPage, page);
    }

    @GetMapping("/recommendation/{contentId}")
    public ResponseEntity<StatusResponse> recommendFiveSpot(@PathVariable Long contentId, @RequestParam(value = "x") String mapX, @RequestParam(value = "y") String mapY) {
        return spotService.getFiveSpotsByDistance(contentId, mapX, mapY);
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<StatusResponse> getSpotDetail(@PathVariable Long contentId) {
        Spot spot = spotService.findSpotByContentId(contentId);
        Object commonDetail = tourApiService.getCommonDetail(spot.getCategory().getCode(), spot.getContentId());
        Object detailInfo = tourApiService.getDetailInfo(spot);
        JSONArray imageArr = tourApiService.getDetailImages(contentId);
        return spotService.responseDetailInfo(commonDetail, detailInfo, imageArr, spot);
    }

    @GetMapping("/trend")
    public ResponseEntity<StatusResponse> getTrend() {
        return spotService.getTrendMagazine();
    }

    //장소 검색(키워드+카테고리+지역구 각각 필터 가능, 지역구는 다중선택)
    @PostMapping("/search")
    public ResponseEntity<StatusResponse> searchSpot(@RequestBody SpotSearchReqDto spotSearchReqDto, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        //필터링 조건 없으면 전체 조회
        if (spotSearchReqDto.getKeyword().isEmpty() && spotSearchReqDto.getCategory().isEmpty() && spotSearchReqDto.getDistrict().isEmpty()) {
            Page<Spot> spotPage = spotService.findAllSpots(page - 1, size);
            return spotService.changePageToResponse(spotPage, page);
        }
        Page<Spot> spotPage = spotService.getSpotListBySearch(spotSearchReqDto, page - 1, size);
        return spotService.changePageToResponse(spotPage, page);
    }
}