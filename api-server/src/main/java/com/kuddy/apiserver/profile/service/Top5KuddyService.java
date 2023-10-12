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
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final CacheManager contentCacheManager;
    public List<Profile> findTopKuddies() {
        NumberExpression<Integer> reviewScore = calculateReviewScore();
        BooleanExpression highLevelAndRoleCriteria = highLevelKuddyCriteria().and(isKuddyRole());
        BooleanExpression lastSevenDaysCriteria = withinLastSevenDays();

        JPAQuery<Tuple> query = queryFactory
                .select(review.meetup.kuddy, reviewScore.sum().as("totalScore"))
                .from(review)
                .leftJoin(review.meetup.kuddy, member)
                .leftJoin(member.profile, profile)
                .where(highLevelAndRoleCriteria.and(lastSevenDaysCriteria))
                .groupBy(member.id)
                .orderBy(orderByScoreAndDate(reviewScore))
                .limit(5);

        List<Tuple> results = query.fetch();

        return extractProfilesFromResults(results);
    }

    private NumberExpression<Integer> calculateReviewScore() {
        return new CaseBuilder()
                .when(review.grade.eq(Grade.PERFECT)).then(10)
                .when(review.grade.eq(Grade.GOOD)).then(5)
                .when(review.grade.eq(Grade.DISAPPOINT)).then(-5)
                .otherwise(0);
    }

    private BooleanExpression highLevelKuddyCriteria() {
        return member.profile.kuddyLevel.in(KuddyLevel.SOULMATE, KuddyLevel.COMPANION);
    }

    private BooleanExpression isKuddyRole() {
        return member.roleType.eq(RoleType.KUDDY);
    }

    private BooleanExpression withinLastSevenDays() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return review.createdDate.after(sevenDaysAgo.atStartOfDay());
    }

    private OrderSpecifier<?>[] orderByScoreAndDate(NumberExpression<Integer> reviewScore) {
        return new OrderSpecifier[]{
                Expressions.numberTemplate(Integer.class, "{0}", reviewScore.sum()).desc(),
                review.createdDate.desc()
        };
    }

    private List<Profile> extractProfilesFromResults(List<Tuple> results) {
        return results.stream()
                .map(tuple -> tuple.get(review.meetup.kuddy).getProfile())
                .collect(Collectors.toList());
    }



    @Cacheable(value="topKuddies",cacheManager = "contentCacheManager")
    public Top5KuddyListResDto getTop5Kuddy(){
        List<Profile> profiles = findTopKuddies(); // 캐시에 데이터가 없으면 실행
        List<Top5KuddyListResDto.Top5KuddyResDto> resDtos = profiles.stream()
                .map(profile -> {
                    Member member = profile.getMember();
                    String review = findRecentReviewByKuddy(member).getContent(); // Assuming Review has getContent()
                    return Top5KuddyListResDto.Top5KuddyResDto.from(member, review);
                })
                .collect(Collectors.toList());
        return Top5KuddyListResDto.of(resDtos);
    }

    @CacheEvict(value = "topKuddies", allEntries = true, cacheManager = "contentCacheManager" )
    @Scheduled(cron = "0 0 12 * * ?") // 매일 오후 12시에 실행
    public void refreshTopKuddies() {
        getTop5Kuddy();
    }

    public void updateTop5KuddiesCache(Member member){
        Top5KuddyListResDto cachedData = getTop5Kuddy();
        for(Top5KuddyListResDto.Top5KuddyResDto kuddy : cachedData.getTop5KuddyList()) {
            if(kuddy.getMemberId().equals(member.getId())) {
                kuddy.setNickname(member.getNickname());
                kuddy.setProfileImageUrl(member.getProfileImageUrl());
                updateCache(cachedData);  // 변경된 정보로 캐시 업데이트
                break;
            }
        }
    }

    private void updateCache(Top5KuddyListResDto updatedData) {
        Cache cache = contentCacheManager.getCache("topKuddies");
        if (cache != null) {
            cache.put("topKuddies", updatedData);
        }
    }

    @Transactional(readOnly = true)
    public Review findRecentReviewByKuddy(Member member){
        return reviewRepository.findFirstByMeetupKuddyIdOrderByCreatedDateDesc(member.getId())
                .orElseThrow(ReviewNotFoundException::new);
    }


}
