package com.kuddy.apiserver.spot.service;

import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import com.kuddy.common.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;

    //필요한 정보(이름, 지역, 카테고리, 사진, 고유id)만 db에 저장
    public void changeAndSave(JSONArray spotArr) {
        for (Object o : spotArr) {
            JSONObject item = (JSONObject) o;
            String contentType = "";
            String areaCode = "";

            if(!spotRepository.existsByContentId(Long.valueOf((String)item.get("contentid")))) {
                for(Category category : Category.values()) {
                    if(item.get("contenttypeid").equals(category.getCode()))
                        contentType = category.name();
                }
                for(District district : District.values()) {
                    if(item.get("sigungucode").equals(district.getCode()))
                        areaCode = district.name();
                }

                spotRepository.save(Spot.builder()
                        .name((String) item.get("title"))
                        .contentId(Long.valueOf((String)item.get("contentid")))
                        .district(District.valueOf(areaCode))
                        .category(Category.valueOf(contentType))
                        .imageUrl((String) item.get("firstimage"))
                        .build());
            }
        }
    }
}

