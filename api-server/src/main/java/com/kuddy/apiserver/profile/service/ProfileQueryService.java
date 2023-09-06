package com.kuddy.apiserver.profile.service;

import static com.kuddy.common.member.domain.Member.*;
import static com.kuddy.common.member.domain.QMember.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.common.profile.domain.KuddyLevel;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.QProfile;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
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

}
