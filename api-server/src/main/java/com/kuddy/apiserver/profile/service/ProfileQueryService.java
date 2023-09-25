package com.kuddy.apiserver.profile.service;

import static com.kuddy.common.member.domain.Member.*;
import static com.kuddy.common.member.domain.QMember.*;

import static com.kuddy.common.profile.domain.QProfileArea.*;
import static com.kuddy.common.review.domain.QReview.review;

import java.util.List;
import java.util.stream.Collectors;

import com.kuddy.common.member.domain.RoleType;

import com.kuddy.common.profile.domain.*;
import com.kuddy.common.review.domain.Grade;
import com.querydsl.core.Tuple;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.apiserver.profile.dto.request.ProfileSearchReqDto;
import com.kuddy.common.member.domain.Member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileQueryService {
	private final JPAQueryFactory queryFactory;

	QProfile profile = QProfile.profile;


	public Page<Profile> findAllExcludeNotKuddyOrderedByKuddyLevel(Pageable pageable) {
		OrderSpecifier<Integer> orderSpecifier = new CaseBuilder()
				.when(profile.kuddyLevel.stringValue().eq("SOULMATE")).then(1)
				.when(profile.kuddyLevel.stringValue().eq("HARMONY")).then(2)
				.when(profile.kuddyLevel.stringValue().eq("FRIEND")).then(3)
				.when(profile.kuddyLevel.stringValue().eq("EXPLORER")).then(4)
				.otherwise(9999)
				.asc();

		List<Profile> profiles = queryFactory
				.selectFrom(profile)
				.leftJoin(profile.member, member)
				.where(member.roleType.eq(RoleType.KUDDY)
						.and(member.nickname.ne(FORBIDDEN_WORD)))
				.groupBy(profile.id)
				.orderBy(orderSpecifier, profile.createdDate.desc())  // Added orderSpecifier here
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(profile.count())
				.from(profile)
				.leftJoin(profile.member, member)
				.where(member.roleType.eq(RoleType.KUDDY)
						.and(member.nickname.ne(FORBIDDEN_WORD)));

		return PageableExecutionUtils.getPage(profiles, pageable, countQuery::fetchOne);
	}




	public Page<Profile> findProfilesTravelerOrderedByCreatedDate(Pageable pageable) {

		List<Profile> profiles = queryFactory
				.selectFrom(profile)
				.leftJoin(profile.member, member)
				.where(member.roleType.eq(RoleType.TRAVELER)
						.and(member.nickname.ne(FORBIDDEN_WORD)))
				.groupBy(profile.id)
				.orderBy(profile.createdDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(profile.count())
				.from(profile)
				.leftJoin(profile.member, member)
				.where(member.roleType.eq(RoleType.TRAVELER)
						.and(member.nickname.ne(FORBIDDEN_WORD)));


		return PageableExecutionUtils.getPage(profiles, pageable, countQuery::fetchOne);
	}
	public List<Profile> findProfilesByMemberNickname(String nickname) {

		BooleanExpression predicate = member.nickname.likeIgnoreCase("%" + nickname + "%")
			.and(member.nickname.ne(Member.FORBIDDEN_WORD)); // FORBIDDEN_WORD를 제외

		NumberExpression<Integer> caseOrder = Expressions.cases()
			.when(member.nickname.equalsIgnoreCase(nickname)).then(0)
			.otherwise(1);

		NumberExpression<Integer> lengthOrder = Expressions.numberTemplate(Integer.class, "LENGTH({0})", member.nickname);

		return queryFactory
			.select(profile)
			.from(profile)
			.join(profile.member, member)
			.where(predicate)
			.orderBy(
				caseOrder.asc(),
				lengthOrder.asc(),
				profile.createdDate.desc()
			)
			.fetch();

	}


	public Page<Profile> findProfilesBySearchCriteria(int page, int size, ProfileSearchReqDto searchCriteria) {
		Pageable pageable = PageRequest.of(page, size);
		BooleanBuilder builder = new BooleanBuilder();

		// GenderType 검색 조건
		if (!searchCriteria.getGenderType().isBlank()) {
			builder.and(profile.genderType.eq(GenderType.fromString(searchCriteria.getGenderType())));
		}

		// Area 검색 조건 (district)
		if (!searchCriteria.getAreaName().isBlank()) {
			builder.and(profileArea.area.district.eq(searchCriteria.getAreaName()));
		}

		// Interest Group & Content 조건
		if (!searchCriteria.getInterestGroup().isBlank() && !searchCriteria.getInterestContent().isBlank()) {
			BooleanExpression interestCondition = buildInterestCondition(
					searchCriteria.getInterestGroup(), searchCriteria.getInterestContent()
			);
			builder.and(interestCondition);
		}

		// Nickname 검색 조건
		if (!searchCriteria.getNickname().isBlank()) {
			BooleanExpression predicate = member.nickname.likeIgnoreCase("%" + searchCriteria.getNickname() + "%")
					.and(member.nickname.ne(Member.FORBIDDEN_WORD));
			builder.and(predicate);
		}

		NumberExpression<Integer> caseOrder = Expressions.cases()
				.when(member.nickname.equalsIgnoreCase(searchCriteria.getNickname())).then(0)
				.otherwise(1);

		NumberExpression<Integer> lengthOrder = Expressions.numberTemplate(Integer.class, "LENGTH({0})", member.nickname);

		// RoleType 필터링
		if (!searchCriteria.getRole().isBlank()) {
			RoleType roleType = RoleType.fromString(searchCriteria.getRole());
			builder.and(member.roleType.eq(roleType));
		} else {
			BooleanExpression roleCondition = member.roleType.eq(RoleType.KUDDY)
					.or(member.roleType.eq(RoleType.TRAVELER));
			builder.and(roleCondition);
		}

		List<Profile> profiles = queryFactory
				.selectFrom(profile).distinct()
				.leftJoin(profile.districts, profileArea)
				.join(profile.member, member)
				.where(builder)
				.orderBy(
						caseOrder.asc(),
						lengthOrder.asc(),
						profile.createdDate.desc()
				)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		JPAQuery<Long> countQuery = queryFactory
				.select(profile.count())
				.from(profile)
				.leftJoin(profile.districts, profileArea)
				.join(profile.member, member)
				.where(builder);


		return PageableExecutionUtils.getPage(profiles, pageable, countQuery::fetchOne);

	}
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

	private BooleanExpression buildInterestCondition(String interestGroup, String interestContent) {
		switch (interestGroup) {
			case "investmentTech":
				return profile.activitiesInvestmentTechs.contains(
					ActivitiesInvestmentTech.valueOf(interestContent)
				);
			case "artBeauty":
				return profile.artBeauties.contains(
					ArtBeauty.valueOf(interestContent)
				);
			case "careerMajor":
				return profile.careerMajors.contains(
					CareerMajor.valueOf(interestContent)
				);
			case "lifestyle":
				return profile.lifestyles.contains(
					Lifestyle.valueOf(interestContent)
				);
			case "entertainment":
				return profile.entertainments.contains(
					Entertainment.valueOf(interestContent)
				);
			case "food":
				return profile.foods.contains(
					Food.valueOf(interestContent)
				);
			case "hobbiesInterests":
				return profile.hobbiesInterests.contains(
					HobbiesInterests.valueOf(interestContent)
				);
			case "sports":
				return profile.sports.contains(
					Sports.valueOf(interestContent)
				);
			case "wellbeing":
				return profile.wellbeings.contains(
					Wellbeing.valueOf(interestContent)
				);
			default:
				return null;
		}
	}


}
