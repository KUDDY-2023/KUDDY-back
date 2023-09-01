package com.kuddy.common.community.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kuddy.common.spot.domain.District;

public class ItinerarySpot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	private District district;

	private BigDecimal lat;
	private BigDecimal lng;
	private String place;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

}
