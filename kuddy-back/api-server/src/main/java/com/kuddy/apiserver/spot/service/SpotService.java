package com.kuddy.apiserver.spot.service;

import com.kuddy.apiserver.spot.dto.SpotDetailResDto;
import com.kuddy.apiserver.spot.dto.SpotPageResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;

    //JSON 응답값 중 필요한 정보(이름, 지역, 카테고리, 사진, 고유id)만 db에 저장
    public void changeAndSave(JSONArray spotArr) {
        for (Object o : spotArr) {
            JSONObject item = (JSONObject) o;
            String contentType = "";
            String areaCode = "";

            if (!spotRepository.existsByContentId(Long.valueOf((String) item.get("contentid")))) {
                for (Category category : Category.values()) {
                    if (item.get("contenttypeid").equals(category.getCode()))
                        contentType = category.name();
                }
                for (District district : District.values()) {
                    if (item.get("sigungucode").equals(district.getCode()))
                        areaCode = district.name();
                }

                spotRepository.save(Spot.builder()
                        .name((String) item.get("title"))
                        .contentId(Long.valueOf((String) item.get("contentid")))
                        .district(District.valueOf(areaCode))
                        .category(Category.valueOf(contentType))
                        .imageUrl((String) item.get("firstimage"))
                        .build());
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<Spot> findSpotByCategory(String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAllByCategory(Category.valueOf(category), pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Spot> findSpotByDistrict(String district, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAllByDistrict(District.valueOf(district), pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Spot> findAllSpots(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAll(pageRequest);
    }

    @Transactional(readOnly = true)
    public Spot findSpotByContentId(Long contentId) {
        return spotRepository.findByContentId(contentId);
    }

    //조회한 spot 리스트와 페이지 정보를 공통응답형식으로 반환하도록 변환하는 메소드
    public ResponseEntity<StatusResponse> changePageToResponse(Page<Spot> spotPage, int page, int size) {
        List<Spot> spotList = spotPage.getContent();
        List<SpotResDto> respone = new ArrayList<>();
        for (Spot spot : spotList) {
            SpotResDto spotResDto = SpotResDto.of(spot);
            respone.add(spotResDto);
        }

        //페이지가 1장일 경우 요소의 총 개수가 size
        if (spotPage.getTotalPages() == 1) {
            size = (int) spotPage.getTotalElements();
        }

        PageInfo pageInfo = new PageInfo(page, size, spotPage.getTotalElements(), spotPage.getTotalPages());
        SpotPageResDto spotPageResDto = new SpotPageResDto(respone, pageInfo);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(spotPageResDto)
                .build());
    }


    //Tour Api에서 조회한 정보를 db에 저장하지 않고 공통응답형식으로 반환하도록 변환하는 메소드(위치 기반 추천)
    public ResponseEntity<StatusResponse> changeJsonToResponse(JSONObject body) {
        int numOfRows = Integer.parseInt(String.valueOf(body.get("numOfRows")));
        int pageNo = Integer.parseInt(String.valueOf(body.get("pageNo")));
        long totalCount = (long) body.get("totalCount");

        List<SpotResDto> response = changeJsonBodyToList(body);
        PageInfo pageInfo = new PageInfo(pageNo, numOfRows, totalCount, (int) (totalCount / 20 + 1));
        SpotPageResDto spotPageResDto = new SpotPageResDto(response, pageInfo);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(spotPageResDto)
                .build());
    }

    //각 관광지 상세 정보 조회(사진 여러장 + 찜한 멤버들 + 위치기반추천)
    public ResponseEntity<StatusResponse> responseDetailInfo(Object o, JSONObject body, JSONArray imageArr, Spot spot) {

        //위치 기반 관광지 추천(5개)
        List<SpotResDto> recommendationList = changeJsonBodyToList(body);

        //이미지
        List<String> imageList = new ArrayList<>();
        imageList.add(spot.getImageUrl());
        for(Object object : imageArr) {
            JSONObject item = (JSONObject) object;
            String imageUrl = (String) item.get("originimgurl");
            imageList.add(imageUrl);
        }

        //상세 정보
        JSONObject item = (JSONObject) o;
        SpotDetailResDto spotDetailResDto = SpotDetailResDto.of(spot, (String) item.get("overview"), (String) item.get("tel"),
                (String) item.get("homepage"), (String) item.get("addr1"), (String) item.get("zipcode"), recommendationList, imageList);

        return ResponseEntity.ok(StatusResponse.builder()
                        .status(StatusEnum.OK.getStatusCode())
                        .message(StatusEnum.OK.getCode())
                        .data(spotDetailResDto)
                        .build());
    }

    //JSONObject에서 필요한 정보만 담아 List로 반환하는 반복적인 코드
    public List<SpotResDto> changeJsonBodyToList(JSONObject body) {
        JSONObject items = (JSONObject) body.get("items");
        JSONArray spotArr = (JSONArray) items.get("item");

        List<SpotResDto> response = new ArrayList<>();
        for (Object o : spotArr) {
            JSONObject item = (JSONObject) o;
            String contentType = "";
            String areaCode = "";
            for (Category category : Category.values()) {
                if (item.get("contenttypeid").equals(category.getCode()))
                    contentType = category.getType();
            }
            for (District district : District.values()) {
                if (item.get("sigungucode").equals(district.getCode()))
                    areaCode = district.getArea();
            }

            SpotResDto spotResDto = SpotResDto.builder()
                    .name((String) item.get("title"))
                    .contentId(Long.valueOf((String) item.get("contentid")))
                    .district(areaCode)
                    .category(contentType)
                    .imageUrl((String) item.get("firstimage"))
                    .mapX(String.valueOf(item.get("mapx")))
                    .mapY(String.valueOf(item.get("mapy")))
                    .build();
            response.add(spotResDto);
        }
        return response;
    }
}
