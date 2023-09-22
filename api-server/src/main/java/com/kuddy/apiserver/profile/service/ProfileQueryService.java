package com.kuddy.apiserver.profile.service;

import static com.kuddy.common.member.domain.Member.*;
import static com.kuddy.common.member.domain.QMember.*;
import static com.kuddy.common.profile.domain.QProfileArea.*;

import java.util.List;

import com.kuddy.common.profile.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
			.when(profile.kuddyLevel.stringValue().eq("COMPANION")).then(3)
			.when(profile.kuddyLevel.stringValue().eq("FRIENDZONE")).then(4)
			.when(profile.kuddyLevel.stringValue().eq("EXPLORER")).then(5)
			.otherwise(9999)
			.asc();

		List<Profile> profiles = queryFactory.selectFrom(profile)
			.where(profile.kuddyLevel.ne(KuddyLevel.NOT_KUDDY))
			.orderBy(orderSpecifier)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(profile.count())
			.from(profile)
			.where(profile.kuddyLevel.ne(KuddyLevel.NOT_KUDDY))
			.fetchOne();

		return new PageImpl<>(profiles, pageable, total);
	}



	public Page<Profile> findProfilesTravelerOrderedByCreatedDate(Pageable pageable) {

		List<Profile> profiles = queryFactory
			.selectFrom(profile)
			.leftJoin(profile.member, member)
			.where(profile.kuddyLevel.eq(KuddyLevel.NOT_KUDDY)
				.and(member.nickname.ne(FORBIDDEN_WORD)))  // 추가한 조건
			.groupBy(profile.id)
			.orderBy(profile.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(profile.count())
			.from(profile)
			.leftJoin(profile.member, member)
			.where(profile.kuddyLevel.eq(KuddyLevel.NOT_KUDDY)
				.and(member.nickname.ne("unknown")))
			.fetchOne();

		return new PageImpl<>(profiles, pageable, total);
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


	public List<Profile> findProfilesBySearchCriteria(ProfileSearchReqDto searchCriteria) {
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

		return queryFactory
				.selectFrom(profile)
				.leftJoin(profile.districts, profileArea)
				.join(profile.member, member)  // Assumes a join with the member table
				.where(builder)
				.orderBy(
						caseOrder.asc(),
						lengthOrder.asc(),
						profile.createdDate.desc()
				)
				.fetch();

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
				return null; // 또는 적절한 예외를 던집니다.
		}
	}





}
