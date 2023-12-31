package com.kuddy.apiserver.spot.service;

import com.kuddy.apiserver.member.dto.PickMemberResDto;
import com.kuddy.apiserver.spot.dto.SpotSearchReqDto;
import com.kuddy.apiserver.spot.dto.SpotDetailResDto;
import com.kuddy.apiserver.spot.dto.SpotPageResDto;
import com.kuddy.apiserver.spot.dto.SpotResDto;
import com.kuddy.common.heart.domain.Heart;
import com.kuddy.common.heart.repository.HeartRepository;
import com.kuddy.common.member.domain.MemberStatus;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.page.PageInfo;
import com.kuddy.common.profile.exception.ProfileNotFoundException;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.NoSpotNearbyException;
import com.kuddy.common.spot.exception.SpotNotFoundException;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kuddy.common.spot.repository.SpotSpecification.*;

@Service
@Transactional
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class SpotService {

    private final SpotRepository spotRepository;
    private final HeartRepository heartRepository;
    private final SpotQueryService spotQueryService;
    private final TourApiService tourApiService;

    @Scheduled(cron = "0 0 6 * * *")
    public void syncTourDataEveryDay() {
        LocalDate seoulNow = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate oneDayAgo = seoulNow.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String modifiedTime = oneDayAgo.format(formatter);
        log.info(modifiedTime);
        synchronizeTourData(tourApiService.getSyncList(1, 50, modifiedTime));
    }

    public void synchronizeTourData(JSONArray spotArr) {
        for (Object o : spotArr) {
            JSONObject item = (JSONObject) o;
            Long contentId = Long.valueOf((String) item.get("contentid"));
            log.info("sync contentId = " + contentId);

            if(Category.hasValue((String) item.get("contenttypeid"))) {
                //이미 저장되어 있을 경우 내용 업데이트
                if (spotRepository.existsByContentId(contentId)) {
                    updateSpot(item, contentId);
                }
                //표출되고 있는 데이터이고 db에 저장되어 있지 않은 경우 새로 저장
                if ((item.get("showflag").equals("1")) && (!spotRepository.existsByContentId(contentId))) {
                    saveSpot(item, contentId);
                    addImage(contentId);
                }
            }
        }
    }

    public void updateSpot(JSONObject item, Long contentId) {
        Spot spot = findSpotByContentId(contentId);
        //더이상 표출되지 않는 데이터이면 내용 비우기
        if(item.get("showflag").equals("0")) {
            spot.update((String) item.get("title"),
                    District.valueOfCode((String) item.get("sigungucode")),
                    "",
                    Category.valueOfCode((String) item.get("contenttypeid")),
                    "",
                    "",
                    "No more information found for this spot.",
                    (String) item.get("modifiedtime"));
            log.info("update showflag = 0, contentId = " + contentId);
        }
        //표출되고 있고 저장되어 있는 데이터와 modifiedTime이 다르면 정보 업데이트
        if((item.get("showflag").equals("1")) && (!spot.getModifiedTime().equals(item.get("modifiedtime")))) {
            spot.update((String) item.get("title"),
                    District.valueOfCode((String) item.get("sigungucode")),
                    (String) item.get("firstimage"),
                    Category.valueOfCode((String) item.get("contenttypeid")),
                    (String) item.get("mapx"),
                    (String) item.get("mapy"),
                    getSpotAbout(item, contentId),
                    (String) item.get("modifiedtime"));
            log.info("update showflag = 1, contentId = " + contentId);
            addImage(contentId);
        }

    }

    public void saveSpot(JSONObject item, Long contentId) {
        log.info("save contentId = " + contentId);
        spotRepository.save(Spot.builder()
                .name((String) item.get("title"))
                .contentId(contentId)
                .district(District.valueOfCode((String) item.get("sigungucode")))
                .category(Category.valueOfCode((String) item.get("contenttypeid")))
                .imageUrl((String) item.get("firstimage"))
                .numOfHearts(0L)
                .mapX((String) item.get("mapx"))
                .mapY((String) item.get("mapy"))
                .about(getSpotAbout(item, contentId))
                .modifiedTime((String) item.get("modifiedtime"))
                .build());
    }

    //TourAPI에서 spot about만 가져오기
    public String getSpotAbout(JSONObject item, Long contentId) {
        String about;
        Object commonDetail = tourApiService.getCommonDetail((String) item.get("contenttypeid"), contentId);
        JSONObject detail = (JSONObject) commonDetail;
        about = (String) detail.get("overview");
        return about;

    }

    //썸네일 없을때 상세 이미지들 중 첫번째 사진을 썸네일로 지정
    public void addImage(Long contentId) {
        Spot spot = findSpotByContentId(contentId);
        if(spot.getImageUrl().isEmpty()){
            JSONArray imageArr = tourApiService.getDetailImages(contentId);
            if(imageArr == null)
                spot.setImageUrl("");
            if(!(imageArr == null)) {
                JSONObject detailImage = (JSONObject) imageArr.get(0);
                spot.setImageUrl((String) detailImage.get("originimgurl"));
            }
            log.info("addImage URL = " + spot.getImageUrl());
        }
    }

    @Transactional(readOnly = true)
    public Page<Spot> findSpotByCategory(String category, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAllByCategoryOrderByNumOfHeartsDesc(Category.valueOf(category), pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Spot> findSpotByDistrict(String district, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAllByDistrictOrderByNumOfHeartsDesc(District.valueOf(district), pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Spot> findAllSpots(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return spotRepository.findAllByOrderByNumOfHeartsDesc(pageRequest);
    }

    @Transactional(readOnly = true)
    public Spot findSpotByContentId(Long contentId) {
        Spot spot = spotRepository.findByContentId(contentId);
        if(spot == (null))
            throw new SpotNotFoundException(contentId);
        return spot;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StatusResponse> getTrendMagazine() {
        List<Spot> spotList = spotRepository.findTop5ByOrderByNumOfHeartsDesc();
        return returnStatusResponse(changeListToDto(spotList));
    }


    public Page<Spot> getSpotsByDistance(int page, String mapX, String mapY) {
        List<Spot> spotList = spotRepository.findAllByDistance(mapX, mapY);
        if(spotList.isEmpty())
            throw new NoSpotNearbyException();
        PageRequest pageRequest = PageRequest.of(page, 20);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), spotList.size());
        Page<Spot> spotPage = new PageImpl<>(spotList.subList(start, end), pageRequest, spotList.size());
        return spotPage;
    }

    //위치 기반 spot 5개만 조회
    public ResponseEntity<StatusResponse> getFiveSpotsByDistance(Long contentId, String mapX, String mapY) {
        List<Spot> spotList = spotRepository.findTop5ByDistance(contentId, mapX, mapY);
        if(spotList.isEmpty())
            throw new NoSpotNearbyException();
        return returnStatusResponse(changeListToDto(spotList));
    }


    //조회한 spot 리스트와 페이지 정보를 공통응답형식으로 반환하도록 변환하는 메소드
    public ResponseEntity<StatusResponse> changePageToResponse(Page<Spot> spotPage, int page) {
        List<Spot> spotList = spotPage.getContent();
        List<SpotResDto> response = changeListToDto(spotList);

        PageInfo pageInfo = new PageInfo(page, spotPage.getNumberOfElements(), spotPage.getTotalElements(), spotPage.getTotalPages());
        SpotPageResDto spotPageResDto = new SpotPageResDto(response, pageInfo);

        return returnStatusResponse(spotPageResDto);
    }

    public List<SpotResDto> changeListToDto(List<Spot> spotList) {
        List<SpotResDto> spotResDtoList = new ArrayList<>();
        for (Spot spot : spotList) {
            SpotResDto spotResDto = SpotResDto.of(spot);
            spotResDtoList.add(spotResDto);
        }
        return spotResDtoList;
    }

    //각 관광지 상세 정보 조회(사진 여러장 + 찜한 멤버들 + 위치기반추천)
    public ResponseEntity<StatusResponse> responseDetailInfo(Object commonDetail, Object detailInfo, JSONArray imageArr, Spot spot) {

        //찜한 멤버들
        List<PickMemberResDto> kuddyList = new ArrayList<>();
        List<PickMemberResDto> travelerList = new ArrayList<>();
        for(Heart heart : heartRepository.findAllBySpot(spot)) {
            if(heart.getMember().getProfile() == null)
                throw new ProfileNotFoundException();
            if(heart.getMember().getRoleType().equals(RoleType.KUDDY) && !heart.getMember().getMemberStatus().equals(
                    MemberStatus.WITHDRAW))
                kuddyList.add(PickMemberResDto.of(heart.getMember()));
            if(heart.getMember().getRoleType().equals(RoleType.TRAVELER) && !heart.getMember().getMemberStatus().equals(
                    MemberStatus.WITHDRAW))
                travelerList.add(PickMemberResDto.of(heart.getMember()));
        }

        //이미지
        List<String> imageList = new ArrayList<>();
        if(!(imageArr == null)) {
            for (Object object : imageArr) {
                JSONObject item = (JSONObject) object;
                String imageUrl = (String) item.get("originimgurl");
                imageList.add(imageUrl);
            }
        }
        if(imageList.isEmpty() && !spot.getImageUrl().isEmpty()) {
            imageList.add(spot.getImageUrl());
        }

        //상세 정보
        JSONObject additionalInfo = (JSONObject) detailInfo;
        additionalInfo.remove("contenttypeid");
        additionalInfo.remove("contentid");

        //공통 정보
        JSONObject item = (JSONObject) commonDetail;
        SpotDetailResDto spotDetailResDto = SpotDetailResDto.of(spot, (String) item.get("overview"), (String) item.get("tel"),
                (String) item.get("homepage"), (String) item.get("addr1"), (String) item.get("zipcode"), (Object) additionalInfo,
                imageList, kuddyList, travelerList);

        return returnStatusResponse(spotDetailResDto);
    }

    private ResponseEntity<StatusResponse> returnStatusResponse(Object data) {
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(data)
                .build());
    }

    public Page<Spot> getSpotListBySearch(SpotSearchReqDto spotSearchReqDto, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("numOfHearts").descending());

        Specification<Spot> spec = (root, query, criteriaBuilder) -> null;
        if(!spotSearchReqDto.getKeyword().isEmpty()) {
            String searchKeyword = spotSearchReqDto.getKeyword().replaceAll(" ", "");
            List<Spot> spotList = spotQueryService.findSpotByNameContains(searchKeyword);
            List<Long> contentIdList = spotList.stream()
                    .map(Spot::getContentId)
                    .collect(Collectors.toList());
            spec = spec.and(matchSpotList(contentIdList));
        }
        if(!spotSearchReqDto.getCategory().isEmpty()) {
            spec = spec.and(equalCategory(Category.valueOf(spotSearchReqDto.getCategory().toUpperCase())));
        }
        if(!spotSearchReqDto.getDistrict().isEmpty()) {
            List<District> districtList = new ArrayList<>();
            for(String districtName : spotSearchReqDto.getDistrict()) {
                districtList.add(District.valueOf(districtName.toUpperCase()));
            }
            spec = spec.and(belongsToDistrict(districtList));
        }
        return spotRepository.findAll(spec, pageable);
    }
}
