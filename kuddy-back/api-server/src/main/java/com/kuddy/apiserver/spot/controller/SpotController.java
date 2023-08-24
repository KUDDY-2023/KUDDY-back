package com.kuddy.apiserver.spot.controller;

import com.kuddy.apiserver.spot.service.TourApiService;
import com.kuddy.apiserver.spot.service.SpotService;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final TourApiService tourApiService;

    //테스트용으로 관광정보 저장하는 api
    @PostMapping("/test")
    public ResponseEntity<StatusResponse> saveTestData(@RequestParam(value = "page") int page, @RequestParam(value = "category") int category) {
        spotService.changeAndSave(tourApiService.getApiDataList(page, category));

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .build());
    }
}