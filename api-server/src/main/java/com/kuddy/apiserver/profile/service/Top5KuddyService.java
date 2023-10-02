package com.kuddy.apiserver.profile.service;

import com.kuddy.apiserver.profile.dto.response.Top5KuddyListResDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.review.domain.Grade;
import com.kuddy.common.review.domain.Review;
import com.kuddy.common.review.exception.ReviewNotFoundException;
import com.kuddy.common.review.repository.ReviewRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.kuddy.common.member.domain.QMember.member;
import static com.kuddy.common.profile.domain.QProfile.profile;
import static com.kuddy.common.review.domain.QReview.review;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Top5KuddyService {
    private final ReviewRepository reviewRepository;
    private final JPAQueryFactory queryFactory;

    @Cacheable(value="topKuddies",cacheManager = "contentCacheManager")
    public List<Profile> findTopKuddies() {
        // 점수 계산
        NumberExpression<Integer> reviewScore = new CaseBuilder()
                .when(review.grade.eq(Grade.PERFECT)).then(10)
                .when(review.grade.eq(Grade.GOOD)).then(5)
                .when(review.grade.eq(Grade.DISAPPOINT)).then(-5)
                .otherwise(0);

        // 최우선은 KuddyLevel이 'SOULMATE'와 'COMPANION'인 멤버
        BooleanExpression isHighLevelKuddy = member.profile.kuddyLevel.eq(KuddyLevel.SOULMATE)
                .or(member.profile.kuddyLevel.eq(KuddyLevel.COMPANION));

        // RoleType이 "KUDDY"인 멤버만
        BooleanExpression isKuddyRole = member.roleType.eq(RoleType.KUDDY);

        JPAQuery<Tuple> query = queryFactory
                .select(review.meetup.kuddy, reviewScore.sum().as("totalScore"))
                .from(review)
                .leftJoin(review.meetup.kuddy, member)
                .leftJoin(member.profile, profile)
                .where(isHighLevelKuddy.and(isKuddyRole))  // 조건: 높은 레벨의 Kuddy만 + RoleType이 "KUDDY"인 경우
                .groupBy(member.id)
                .orderBy(
                        // 점수 높은 순, 그리고 최신 리뷰 날짜 순
                        Expressions.numberTemplate(Integer.class, "{0}", reviewScore.sum()).desc(),
                        review.createdDate.desc()
                )
                .limit(5);

        List<Tuple> results = query.fetch();

        return results.stream()
                .map(tuple -> tuple.get(review.meetup.kuddy).getProfile())
                .collect(Collectors.toList());
    }

    public Top5KuddyListResDto changePageToResponse(List<Profile> profiles){
        List<Top5KuddyListResDto.Top5KuddyResDto> resDtos = profiles.stream()
                .map(profile -> {
                    Member member = profile.getMember();
                    String review = findRecentReviewByKuddy(member).getContent(); // Assuming Review has getContent()
                    return Top5KuddyListResDto.Top5KuddyResDto.from(member, review);
                })
                .collect(Collectors.toList());
        return Top5KuddyListResDto.of(resDtos);
    }

    @CacheEvict(value = "topKuddies", allEntries = true,cacheManager = "contentCacheManager" )
    @Scheduled(cron = "0 0 12 * * ?") // 매일 오후 12시에 실행
    public void refreshTopKuddies() {
        findTopKuddies();
    }

    @Transactional(readOnly = true)
    public Review findRecentReviewByKuddy(Member member){
        return reviewRepository.findFirstByMeetupKuddyIdOrderByCreatedDateDesc(member.getId())
                .orElseThrow(ReviewNotFoundException::new);
    }


}
