package com.kuddy.common.spot.repository;

import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SpotSpecification {
    //키워드+카테고리+지역구 각각 필터 가능, 지역구는 다중선택

    //키워드 필터
    public static Specification<Spot> containingName(String keyword) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
    }
    //카테고리 필터
    public static Specification<Spot> equalCategory(Category category) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category"), category));
    }

    //지역구 필터
    public static Specification<Spot> belongsToDistrict(List<District> districts){
        return (root, query, criteriaBuilder)->
                criteriaBuilder.in(root.get("district")).value(districts);
    }
}
