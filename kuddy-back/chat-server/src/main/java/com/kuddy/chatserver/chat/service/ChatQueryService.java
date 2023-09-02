package com.kuddy.chatserver.chat.service;

import static com.kuddy.common.chat.domain.QRoom.*;
import static com.kuddy.common.member.domain.QMember.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuddy.chatserver.chat.dto.response.ChatRoomListResDto;
import com.kuddy.common.chat.domain.Room;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {

	private final JPAQueryFactory jpaQueryFactory;
	private final MemberRepository memberRepository;
	private static final String NICKNAME = "nickname";

	// 채팅방 리스트 조회
	// 채팅방 리스트 조회
	public List<ChatRoomListResDto> getChattingList(Long memberId) {
		return jpaQueryFactory.select(Projections.constructor(ChatRoomListResDto.class,
				room.id,
				ExpressionUtils.as(
					JPAExpressions.select(member.nickname)
						.from(member)
						.where(member.id.eq(room.createMember.id)),
					NICKNAME), // createMember
				ExpressionUtils.as(
					JPAExpressions.select(member.nickname)
						.from(member)
						.where(member.id.eq(room.joinMember.id)),
					NICKNAME), // joinMember
				room.createdDate,
				Projections.constructor(ChatRoomListResDto.Participant.class,
					ExpressionUtils.as(
						JPAExpressions.select(member.nickname)
							.from(member)
							.where(member.id.eq(
								new CaseBuilder()
									.when(room.createMember.id.eq(memberId)).then(room.joinMember.id)
									.otherwise(room.createMember.id)
							))
						, NICKNAME),
					ExpressionUtils.as(
						JPAExpressions.select(member.profileImageUrl)
							.from(member)
							.where(member.id.eq(
								new CaseBuilder()
									.when(room.createMember.id.eq(memberId)).then(room.joinMember.id)
									.otherwise(room.createMember.id)
							)), "profile"))
			))
			.from(room)
			.where(room.createMember.id.eq(memberId).or(room.joinMember.id.eq(memberId)))
			.fetch();
	}


	// 현재 메시지를 받는 사람을 조회하는 메서드
	public Member getReceiverNumber(Long chatId, String senderName) {
		Room chatroom = jpaQueryFactory.select(room)
			.from(room)
			.where(room.id.eq(chatId))
			.fetchOne();

		String memberNickname = chatroom.getCreateMember().getNickname().equals(senderName) ?
			chatroom.getJoinMember().getNickname() : chatroom.getCreateMember().getNickname();

		return memberRepository.findByNickname(memberNickname)
			.orElseThrow(MemberNotFoundException::new);
	}


}
