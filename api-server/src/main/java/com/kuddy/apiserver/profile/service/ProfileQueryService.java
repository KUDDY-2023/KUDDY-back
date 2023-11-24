package com.kuddy.apiserver.profile.service;

import static com.kuddy.common.member.domain.Member.*;
import static com.kuddy.common.member.domain.QMember.*;

import static com.kuddy.common.profile.domain.QProfile.profile;
import static com.kuddy.common.profile.domain.QProfileArea.*;


import com.kuddy.common.member.domain.MemberStatus;
import java.util.ArrayList;
import java.util.List;

import com.kuddy.common.member.domain.RoleType;

import com.kuddy.common.profile.domain.*;

import com.querydsl.jpa.impl.JPAQuery;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProfileQueryService {
	private final JPAQueryFactory queryFactory;



	public Page<Profile> findAllExcludeNotKuddyOrderedByKuddyLevel(Pageable pageable) {
		OrderSpecifier<Integer> orderSpecifier = kuddyLevelOrderSpecifier();

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
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
		addDefaultOrder(orderSpecifiers);

		// 각 검색 조건 적용
		applyGenderTypeFilter(searchCriteria, builder);
		applyAreaFilter(searchCriteria, builder);
		applyInterestFilter(searchCriteria, builder);
		applyNicknameFilter(searchCriteria, builder, orderSpecifiers);
		applyRoleTypeFilter(searchCriteria, builder);

		// Query 실행
		List<Profile> profiles = executeQuery(builder, orderSpecifiers,pageable);
		JPAQuery<Long> countQuery = createCountQuery(builder);

		return PageableExecutionUtils.getPage(profiles, pageable, countQuery::fetchOne);
	}

	private void addDefaultOrder(List<OrderSpecifier<?>> orderSpecifiers) {
		OrderSpecifier<Integer> roleOrderSpecifier = kuddyLevelOrderSpecifier();
		orderSpecifiers.add(roleOrderSpecifier);
		orderSpecifiers.add(profile.createdDate.desc());
	}

	private void applyGenderTypeFilter(ProfileSearchReqDto searchCriteria, BooleanBuilder builder) {
		if (!searchCriteria.getGenderType().isBlank()) {
			builder.and(profile.genderType.eq(GenderType.fromString(searchCriteria.getGenderType())));
		}

	}

	private void applyAreaFilter(ProfileSearchReqDto searchCriteria, BooleanBuilder builder) {
		if (!searchCriteria.getAreaName().isBlank()) {
			builder.and(profileArea.area.district.eq(searchCriteria.getAreaName()));
		}
	}

	private void applyInterestFilter(ProfileSearchReqDto searchCriteria, BooleanBuilder builder) {
		if (!searchCriteria.getInterestGroup().isBlank() && !searchCriteria.getInterestContent().isBlank()) {
			BooleanExpression interestCondition = buildInterestCondition(searchCriteria.getInterestGroup(), searchCriteria.getInterestContent());
			builder.and(interestCondition);
		}
	}

	private void applyNicknameFilter(ProfileSearchReqDto searchCriteria, BooleanBuilder builder, List<OrderSpecifier<?>> orderSpecifiers) {
		if (!searchCriteria.getNickname().isBlank()) {
			BooleanExpression predicate = member.nickname.likeIgnoreCase("%" + searchCriteria.getNickname() + "%")
					.and(member.nickname.ne(Member.FORBIDDEN_WORD));
			builder.and(predicate);
			NumberExpression<Integer> caseOrder = Expressions.cases()
					.when(member.nickname.equalsIgnoreCase(searchCriteria.getNickname())).then(0)
					.otherwise(1);

			NumberExpression<Integer> lengthOrder = Expressions.numberTemplate(Integer.class, "LENGTH({0})", member.nickname);
			orderSpecifiers.clear(); // 닉네임의 경우는 이름 정합도만 고려하여 정렬해야함
			orderSpecifiers.add(caseOrder.asc());
			orderSpecifiers.add(lengthOrder.asc());
		}
	}

	private void applyRoleTypeFilter(ProfileSearchReqDto searchCriteria, BooleanBuilder builder) {

		if (!searchCriteria.getRole().isBlank()) {
			RoleType roleType = RoleType.fromString(searchCriteria.getRole());
			builder.and(member.roleType.eq(roleType));
		} else {
			BooleanExpression roleCondition = member.roleType.eq(RoleType.KUDDY)
					.or(member.roleType.eq(RoleType.TRAVELER));
			builder.and(roleCondition);
		}
	}

	private List<Profile> executeQuery(BooleanBuilder builder,List<OrderSpecifier<?>> orderSpecifiers, Pageable pageable) {
		builder.and(member.memberStatus.ne(MemberStatus.WITHDRAW));
		return queryFactory.selectFrom(profile).distinct()
				.leftJoin(profile.districts, profileArea)
				.join(profile.member, member)
				.where(builder)
				.orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	private JPAQuery<Long> createCountQuery(BooleanBuilder builder) {
		return queryFactory.select(profile.count())
				.from(profile)
				.leftJoin(profile.districts, profileArea)
				.join(profile.member, member)
				.where(builder);
	}

	private OrderSpecifier<Integer> kuddyLevelOrderSpecifier(){
		return new CaseBuilder()
				.when(profile.kuddyLevel.stringValue().eq(KuddyLevel.SOULMATE.name())).then(1)
				.when(profile.kuddyLevel.stringValue().eq(KuddyLevel.COMPANION.name())).then(2)
				.when(profile.kuddyLevel.stringValue().eq(KuddyLevel.FRIEND.name())).then(3)
				.when(profile.kuddyLevel.stringValue().eq(KuddyLevel.EXPLORER.name())).then(4)
				.when(profile.kuddyLevel.stringValue().eq(KuddyLevel.NOT_KUDDY.name())).then(5)
				.otherwise(9999)
				.asc();
	}

	private BooleanExpression buildInterestCondition(String interestGroup, String interestContent) {
		switch (interestGroup) {
			case "artBeauty":
				return profile.art.contains(
					Art.valueOf(interestContent)
				);
			case "careerMajor":
				return profile.careers.contains(
					Career.valueOf(interestContent)
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
				return profile.hobbies.contains(
					Hobbies.valueOf(interestContent)
				);
			case "sports":
				return profile.sports.contains(
					Sports.valueOf(interestContent)
				);
			default:
				return null;
		}
	}


}
