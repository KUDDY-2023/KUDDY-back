package com.kuddy.common.chat.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_member_id", referencedColumnName = "member_id")
	private Member createMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "join_member_id", referencedColumnName = "member_id")
	private Member joinMember;

	@Builder
	public Room(Member createMember, Member joinMember) {
		this.createMember = createMember;
		this.joinMember = joinMember;
	}
}
