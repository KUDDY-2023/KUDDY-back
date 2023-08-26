package com.kuddy.apiserver.heart.service;

import com.kuddy.apiserver.heart.dto.HeartResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.heart.domain.Heart;
import com.kuddy.common.heart.exception.HeartNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<StatusResponse> cancelSpotLike(Long id, Member member) {
        Spot spot =  spotRepository.findById(id).orElseThrow(() -> new SpotNotFoundException(id));
        Heart heart = heartRepository.findByMemberAndSpot(member, spot).orElseThrow(HeartNotFoundException::new);
        heartRepository.delete(heart);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(id+"번 관광지 찜 취소")
                .build());

    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> findHeartSpotList(Member member) {
        List<Heart> heartList = heartRepository.findAllByMemberOrderByIdDesc(member);

        List<SpotResDto> spotList = new ArrayList<>();
        for (Heart heart : heartList) {
            Spot spot = spotRepository.findById(heart.getSpot().getId()).orElseThrow(() -> new SpotNotFoundException(heart.getSpot().getId()));
            SpotResDto spotResDto = SpotResDto.of(spot);
            spotList.add(spotResDto);
        }
        Long count = heartRepository.countByMember(member);

        HeartResDto response = new HeartResDto(spotList, member.getId(), count);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(response)
                .build());
    }
}
