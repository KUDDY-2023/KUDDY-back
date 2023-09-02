package com.kuddy.common.member.domain;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class MemberAdapter extends User {
	private Member member;

	public MemberAdapter(Member member) {
		super(member.getEmail(), member.getUsername(), createAuthorities(member.getRoleType()));
		this.member = member;
	}
	private static List<GrantedAuthority> createAuthorities(RoleType roleType) {
		return Collections.singletonList(new SimpleGrantedAuthority(roleType.getCode()));
	}
}
