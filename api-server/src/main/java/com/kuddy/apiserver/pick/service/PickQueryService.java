package com.kuddy.apiserver.pick.service;

import com.kuddy.common.pick.domain.Pick;
import com.kuddy.common.pick.domain.QPick;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PickQueryService {
    private final JPAQueryFactory queryFactory;
    QPick qPick = QPick.pick;

    //Pick의 title 공백 제거해서 keyword와 비교
    public List<Pick> findPickByTitleContains(String keyword){
        return queryFactory
                .select(qPick)
                .from(qPick)
                .where(
                        Expressions.stringTemplate("function('replace',{0},{1},{2})",qPick.title, " ", "").containsIgnoreCase(keyword)
                )
                .fetch();
    }
}
