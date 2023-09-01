package com.kuddy.apiserver.pick.service;

import com.kuddy.apiserver.pick.dto.PickResDto;
import com.kuddy.apiserver.pick.dto.PickSpotResDto;
import com.kuddy.apiserver.pick.dto.ThumbnailListResDto;
import com.kuddy.apiserver.pick.dto.ThumbnailResDto;
import com.kuddy.common.pick.domain.Pick;
import com.kuddy.common.pick.domain.PickSpot;
import com.kuddy.common.pick.exception.PickNotFoundException;
import com.kuddy.common.pick.repository.PickRepository;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PickService {
    private final PickRepository pickRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findRandomThumbnailList() {
        List<Pick> pickList = pickRepository.findAll();
        return changeThumbnailListToResponse(pickList);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findAllThumbnailList() {
        List<Pick> pickList = pickRepository.findAllByOrderById();
        return changeThumbnailListToResponse(pickList);
    }

    //썸네일 리스트 -> 공통 응답형식으로 반환 코드
    public ResponseEntity<StatusResponse> changeThumbnailListToResponse(List<Pick> pickList) {
        List<ThumbnailResDto> thumbnailList = new ArrayList<>();
        for(Pick pick : pickList) {
            ThumbnailResDto thumbnailResDto = ThumbnailResDto.of(pick);
            thumbnailList.add(thumbnailResDto);
        }
        ThumbnailListResDto response = new ThumbnailListResDto(thumbnailList, thumbnailList.size());

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findRandomPickList() {
        List<Pick> pickList = pickRepository.findAll();
        List<PickResDto> response = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            List<PickSpot> pickSpotList = pickList.get(i).getPickSpotList();
            List<PickSpotResDto> pickSpotResDtoList = new ArrayList<>();
            for(PickSpot pickSpot : pickSpotList) {
                pickSpotResDtoList.add(new PickSpotResDto(pickSpot));
            }
            response.add(PickResDto.of(pickList.get(i), pickSpotResDtoList));
        }

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findPick(Long pickId) {
        Pick pick = pickRepository.findById(pickId).orElseThrow(() -> new PickNotFoundException(pickId));
        List<PickSpot> pickSpotList = pick.getPickSpotList();
        List<PickSpotResDto> pickSpotResDtoList = new ArrayList<>();
        for(PickSpot pickSpot : pickSpotList) {
            pickSpotResDtoList.add(PickSpotResDto.of(pickSpot));
        }
        PickResDto response =  PickResDto.of(pick, pickSpotResDtoList);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }
}
