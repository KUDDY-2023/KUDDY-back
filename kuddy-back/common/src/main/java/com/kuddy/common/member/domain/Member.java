package com.kuddy.common.member.domain;


import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.exception.InvalidNicknameException;
import com.kuddy.common.profile.domain.Profile;

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

	@Column(length = 15, nullable = false)
	private String nickname;



	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
	private Profile profile;

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

	public boolean validateNickName(String nickname) {
		if (Objects.isNull(nickname) || nickname.isBlank() || nickname.length() > 15 || !nickname.matches("[a-zA-Z0-9_]+")) {
			throw new InvalidNicknameException();
		}else{
			return true;
		}
	}
	public void updateMember(String email) {
		if (!Objects.equals(this.email, email)) {
			this.email = email;
		}
	}
	public void updateNickname(String nickname) {
		if (validateNickName(nickname) && !Objects.equals(this.nickname, nickname)) {
			this.nickname = nickname;
		}
	}
	public void updateRole(RoleType roleType) {
		if (roleType != null && !Objects.equals(this.roleType, roleType)) {
			this.roleType = roleType;
		}
	}
}
