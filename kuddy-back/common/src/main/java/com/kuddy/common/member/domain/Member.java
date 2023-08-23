package com.kuddy.common.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.kuddy.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", updatable = false)
	private Long id;

	private String username;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(length = 50)
	private String nickname;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private RoleType roleType;


	@Builder
	public Member(String username, String email, String profileImageUrl, String nickname,
		ProviderType providerType, RoleType roleType) {
		this.username = username;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.nickname = nickname;
		this.providerType = providerType;
		this.roleType = roleType;
	}

	public void updateMember(String email){
		this.email = email;
	}

	public void updateNickname(String nickname){
		this.nickname = nickname;
	}


	public void updateRole(RoleType roleType){this.roleType = roleType;}
}
