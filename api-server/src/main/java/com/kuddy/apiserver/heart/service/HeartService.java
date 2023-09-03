package com.kuddy.apiserver.heart.service;

import com.kuddy.apiserver.heart.dto.HeartResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.heart.domain.Heart;
import com.kuddy.common.heart.exception.AlreadyLikedException;
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

    public ResponseEntity<StatusResponse> likeSpot(Long contentId, Member member){
        Spot spot =  findByContentId(contentId);

        if(heartRepository.findByMemberAndSpot(member, spot).isPresent()) {
            throw new AlreadyLikedException();
        } else {
            heartRepository.save(Heart.builder()
                    .member(member)
                    .spot(spot)
                    .build());
            spot.likeSpot();
            return ResponseEntity.ok(StatusResponse.builder()
                    .status(StatusEnum.OK.getStatusCode())
                    .message(StatusEnum.OK.getCode())
                    .data(contentId+"번 관광지 찜")
                    .build());
        }
    }

    public ResponseEntity<StatusResponse> cancelSpotLike(Long contentId, Member member) {
        Spot spot =  findByContentId(contentId);
        Heart heart = heartRepository.findByMemberAndSpot(member, spot).orElseThrow(HeartNotFoundException::new);
        heartRepository.delete(heart);
        spot.cancelSpot();
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(contentId+"번 관광지 찜 취소")
                .build());

    }

    @Transactional(readOnly = true)
    public Spot findByContentId(Long contentId){
        Spot spot = spotRepository.findByContentId(contentId);
        if(spot == (null))
            throw new SpotNotFoundException(contentId);
        return spot;
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
