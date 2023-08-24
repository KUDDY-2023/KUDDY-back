package com.kuddy.apiserver.spot.controller;

import com.kuddy.apiserver.spot.dto.SpotPageResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.apiserver.spot.service.TourApiService;
import com.kuddy.apiserver.spot.service.SpotService;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/category/{category}")
    public ResponseEntity<StatusResponse> getSpotsByCategory(@PathVariable String category, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Spot> spotPage = spotService.findSpotByCategory(category.toUpperCase(), page - 1, size);
        return spotService.changeToResponse(spotPage, page, size);
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<StatusResponse> getSpotsByDistrict(@PathVariable String district, @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Page<Spot> spotPage = spotService.findSpotByDistrict(district.toUpperCase(), page - 1, size);
        return spotService.changeToResponse(spotPage, page, size);
    }
}