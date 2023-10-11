package com.kuddy.apiserver.spot.service;

import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.exception.TourApiExeption;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@Transactional
@RequiredArgsConstructor
public class TourApiService {

    @Value("${tourapi.secret-key}")
    private String SECRET_KEY;
    private final String DEFAULT_QUERY_PARAM = "&MobileOS=ETC&MobileApp=Kuddy&_type=json";

    private static final String BASE_URL = "https://apis.data.go.kr/B551011/EngService1/";

    //카테고리별로 20개씩 조회
    public JSONArray getApiDataList(int page, int size, int category) {

        try {
            URL url = new URL(BASE_URL + "areaBasedList1?numOfRows=" +
                    size +
                    "&pageNo=" +
                    page +
                    DEFAULT_QUERY_PARAM + "&listYN=Y&arrange=A&contentTypeId=" +
                    category +
                    "&areaCode=1&serviceKey="
                    + SECRET_KEY);

            JSONObject items = (JSONObject) extractBody(url).get("items");
            JSONArray spotArr = (JSONArray) items.get("item");

            return spotArr;

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }

    //현재 위치 기반으로 2km 반경 관광지 20개씩 조회
    public JSONObject getLocationBasedApi(int page, int size, String mapX, String mapY) {

        try {
            URL url = new URL(BASE_URL + "locationBasedList1?numOfRows=" +
                    size + "&pageNo=" + page +
                    DEFAULT_QUERY_PARAM + "&listYN=Y&mapX=" +
                    mapX + "&mapY=" + mapY +
                    "&radius=2000&serviceKey="
                    + SECRET_KEY);

            return extractBody(url);

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }

    //각 관광지 공통 정보 조회
    public Object getCommonDetail(String category, Long contentId) {

        try {
            URL url = new URL(BASE_URL + "detailCommon1?contentTypeId=" +
                    category +
                    "&contentId=" +
                    contentId +
                    DEFAULT_QUERY_PARAM + "&defaultYN=Y&addrinfoYN=Y&overviewYN=Y&ServiceKey="
                    + SECRET_KEY);

            JSONObject items = (JSONObject) extractBody(url).get("items");
            JSONArray spotArr = (JSONArray) items.get("item");
            Object item = spotArr.get(0);

            return item;

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }

    //각 관광지 소개 정보 조회
    public Object getDetailInfo(Spot spot) {

        try {
            URL url = new URL(BASE_URL + "detailIntro1?contentId=" +
                    spot.getContentId() + DEFAULT_QUERY_PARAM +
                    "&contentTypeId=" +
                    spot.getCategory().getCode() +
                    "&serviceKey="
                    + SECRET_KEY);

            JSONObject items = (JSONObject) extractBody(url).get("items");
            JSONArray spotArr = (JSONArray) items.get("item");
            Object item = spotArr.get(0);

            return item;

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }

    //이미지 정보 조회 API
    public JSONArray getDetailImages(Long contentId) {
        try {
            URL url = new URL(BASE_URL + "detailImage1?contentId=" +
                    contentId + DEFAULT_QUERY_PARAM +
                    "&serviceKey="
                    + SECRET_KEY);

            //이미지 없을 경우
            if(extractBody(url).get("items").equals(""))
                return null;
            else {
                JSONObject items = (JSONObject) extractBody(url).get("items");
                JSONArray imageArr = (JSONArray) items.get("item");
                return imageArr;
            }



        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }

    }

    //JSON 응답값의 body만 추출하는 반복적인 코드
    public JSONObject extractBody(URL url) {
        try {
            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");

            return body;

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }



}
