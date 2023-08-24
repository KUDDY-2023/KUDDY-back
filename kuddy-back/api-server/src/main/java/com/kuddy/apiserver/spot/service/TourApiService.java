package com.kuddy.apiserver.spot.service;

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

    //카테고리별로 20개씩 조회
    public JSONArray getApiDataList(int page, int category) {

        try {
            String result = "";

            URL url = new URL("https://apis.data.go.kr/B551011/EngService1/areaBasedList1?numOfRows=20&pageNo=" +
                    page +
                    "&MobileOS=ETC&MobileApp=Kuddy&_type=json&listYN=Y&arrange=A&contentTypeId=" +
                    category +
                    "&areaCode=1&serviceKey="
                    + SECRET_KEY);

            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray spotArr = (JSONArray) items.get("item");

            return spotArr;

        } catch(Exception e) {
            e.printStackTrace();
            throw new TourApiExeption();
        }
    }
}
