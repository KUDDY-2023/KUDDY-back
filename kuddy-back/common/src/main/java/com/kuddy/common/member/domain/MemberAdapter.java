package com.kuddy.common.member.domain;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class MemberAdapter extends User { //본 Member에 바로 User을 상속 받으면 도메인 객체는 특정 기능에 종속되므로 Best prac 이 아님
	private Member member;

	public MemberAdapter(Member member) {
		super(member.getEmail(), member.getUsername(), createAuthorities(member.getRoleType()));
		this.member = member;
	}
	private static List<GrantedAuthority> createAuthorities(RoleType roleType) {
		return Collections.singletonList(new SimpleGrantedAuthority(roleType.getCode()));
	}
}
