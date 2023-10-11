package com.kuddy.apiserver.spot.service;


import com.kuddy.apiserver.spot.dto.SpotDetailResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.NoSpotNearbyException;
import com.kuddy.common.spot.exception.SpotRecommendApiException;
import com.kuddy.common.spot.repository.SpotRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
public class RecommendApiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final SpotRepository spotRepository;

    private static final String RECOMMEND_SERVER_URL = "https://api.kuddy.co.kr/recommend/";

    @CacheEvict(value="spotRecommend", key="#contentId", beforeInvocation=true)
    @Cacheable(value="spotRecommend", cacheManager="recommendationCacheManager")
    public List<SpotResDto> getFiveSpotsByDistance(Member member, Long contentId) {
        String url = RECOMMEND_SERVER_URL + contentId + "/" + member.getId();

        List<Long> recommendedSpotIds = fetchRecommendedSpotIds(url);

        if (recommendedSpotIds == null || recommendedSpotIds.isEmpty()) {
            throw new NoSpotNearbyException();
        }

        List<Spot> spotList = spotRepository.findAllById(recommendedSpotIds);

        return changeListToDto(spotList);
    }

    public List<Long> fetchRecommendedSpotIds(String url) {
        try {
            ResponseEntity<List<Long>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Long>>() {}
            );
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error("Error fetching recommended spot IDs", e);
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
