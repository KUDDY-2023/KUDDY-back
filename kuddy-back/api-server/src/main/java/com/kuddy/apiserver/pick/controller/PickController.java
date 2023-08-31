package com.kuddy.apiserver.pick.controller;

import com.kuddy.apiserver.pick.service.PickService;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/picks")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    //랜덤으로 썸네일+제목 리스트 8개 조회
    @GetMapping("/thumbnail")
    public ResponseEntity<StatusResponse> getRandomThumbnailList() {
        return pickService.findRandomThumbnailList();
    }

    //전체 썸네일 리스트 조회(랜덤 아니고 id순)
    @GetMapping
    public ResponseEntity<StatusResponse> getAllThumbnailList() {
        return pickService.findAllThumbnailList();
    }
}
