package com.kuddy.apiserver.pick.service;

import com.kuddy.apiserver.pick.dto.ThumbnailListResDto;
import com.kuddy.apiserver.pick.dto.ThumbnailResDto;
import com.kuddy.common.pick.domain.Pick;
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
}
