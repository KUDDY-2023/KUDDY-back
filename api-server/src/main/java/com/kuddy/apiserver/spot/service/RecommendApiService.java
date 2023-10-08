package com.kuddy.apiserver.spot.service;


import com.kuddy.apiserver.spot.dto.SpotDetailResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.NoSpotNearbyException;
import com.kuddy.common.spot.exception.SpotRecommendApiException;
import com.kuddy.common.spot.exception.TourApiExeption;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendApiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotRepository spotRepository;

    private static final String RECOMMEND_SERVER_URL = "http://localhost:8000/recommend/";

    public List<SpotResDto> getFiveSpotsByDistance(Member member, Long contentId, String mapX, String mapY) {
        try {
            // Recommend 서버 URL에서 장소 ID 가져오기
            String url = RECOMMEND_SERVER_URL + contentId + "/" + member.getId() + "?x=" + mapX + "&y=" + mapY;

            ResponseEntity<List<Long>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            );

            List<Long> recommendedSpotIds = responseEntity.getBody();

            if (recommendedSpotIds == null || recommendedSpotIds.isEmpty()) {
                throw new NoSpotNearbyException();
            }

            // 가져온 장소 ID를 사용하여 상세 정보 조회
            List<Spot> spotList = spotRepository.findAllById(recommendedSpotIds);

            return changeListToDto(spotList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SpotRecommendApiException();
        }
    }

    public List<SpotResDto> changeListToDto(List<Spot> spotList) {
        List<SpotResDto> spotResDtoList = new ArrayList<>();
        for (Spot spot : spotList) {
            SpotResDto spotResDto = SpotResDto.of(spot);
            spotResDtoList.add(spotResDto);
        }
        return spotResDtoList;
    }
}
