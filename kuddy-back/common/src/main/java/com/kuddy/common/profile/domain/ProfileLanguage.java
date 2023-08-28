package com.kuddy.common.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileLanguage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_Language_id", updatable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@ManyToOne
	@JoinColumn(name = "language_id")
	private Language language;

	@Column(length = 5)
	private Integer laguageLevel;

	@Builder
	public ProfileLanguage(Profile profile, Language language, Integer laguageLevel) {
		this.profile = profile;
		this.language = language;
		this.laguageLevel = laguageLevel;
	}

	public void updateLanguageLevel(Integer laguageLevel) {
		this.laguageLevel = laguageLevel;
	}
}
