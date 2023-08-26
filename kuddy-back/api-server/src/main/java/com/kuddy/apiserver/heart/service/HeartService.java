package com.kuddy.apiserver.heart.service;

import com.kuddy.common.heart.domain.Heart;
import com.kuddy.common.heart.repository.HeartRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.SpotNotFoundException;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final SpotRepository spotRepository;

    public ResponseEntity<StatusResponse> likeSpot(Long id, Member member){
        Spot spot =  spotRepository.findById(id).orElseThrow(() -> new SpotNotFoundException(id));

        if(heartRepository.findByMemberAndSpot(member, spot).isPresent()) {
            return ResponseEntity.ok(StatusResponse.builder()
                    .status(StatusEnum.BAD_REQUEST.getStatusCode())
                    .message(StatusEnum.BAD_REQUEST.getCode())
                    .data("이미 찜한 관광지입니다.")
                    .build());
        } else {
            Heart heart = heartRepository.save(Heart.builder()
                    .member(member)
                    .spot(spot)
                    .build());
            return ResponseEntity.ok(StatusResponse.builder()
                    .status(StatusEnum.OK.getStatusCode())
                    .message(StatusEnum.OK.getCode())
                    .data(heart)
                    .build());
        }
    }
}
