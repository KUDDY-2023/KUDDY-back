package com.kuddy.apiserver.spot.service;

import com.kuddy.common.spot.domain.QSpot;
import com.kuddy.common.spot.domain.Spot;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SpotQueryService {
    private final JPAQueryFactory queryFactory;
    QSpot qSpot = QSpot.spot;

    //Spot의 name 공백 제거해서 keyword와 비교
    public List<Spot> findSpotByNameContains(String keyword){
        return queryFactory
                .select(qSpot)
                .from(qSpot)
                .where(
                        Expressions.stringTemplate("function('replace',{0},{1},{2})",qSpot.name, " ", "").containsIgnoreCase(keyword)
                )
                .fetch();
    }
}
