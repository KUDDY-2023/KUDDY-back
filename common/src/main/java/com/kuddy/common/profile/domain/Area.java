package com.kuddy.common.profile.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Area {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "area_id", updatable = false)
	private Long id;

	@Column(length = 50)
	private String city;

	@Column(length = 15)
	private String district;

	@Builder
	public Area(String city, String district) {
		this.city = city;
		this.district = district;
	}
}
