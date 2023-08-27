package com.kuddy.common.profile;

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
public class Profile extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id", updatable = false)
	private Long id;

	@Column(length = 10)
	private String job;

	private Integer kuddyLevel;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private ActivitiesInvestmentTech activitiesInvestmentTech;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private ArtBeauty artBeauty;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private CareerMajor careerMajor;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private DecisionMaking decisionMaking;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Entertainment entertainment;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Food food;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private GenderType genderType;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Hobby hobby;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Sports sports;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Temperament temperament;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Wellbeing wellbeing;


	@Builder
	public Profile(String job, Integer kuddyLevel, ActivitiesInvestmentTech activitiesInvestmentTech,
		ArtBeauty artBeauty, CareerMajor careerMajor, DecisionMaking decisionMaking, Entertainment entertainment,
		Food food,
		GenderType genderType, Hobby hobby, Sports sports, Temperament temperament, Wellbeing wellbeing) {
		this.job = job;
		this.kuddyLevel = kuddyLevel;
		this.activitiesInvestmentTech = activitiesInvestmentTech;
		this.artBeauty = artBeauty;
		this.careerMajor = careerMajor;
		this.decisionMaking = decisionMaking;
		this.entertainment = entertainment;
		this.food = food;
		this.genderType = genderType;
		this.hobby = hobby;
		this.sports = sports;
		this.temperament = temperament;
		this.wellbeing = wellbeing;
	}
	public void changeJob(String newJob) {
		this.job = newJob;
	}

	public void updateKuddyLevel(Integer newKuddyLevel) {
		this.kuddyLevel = newKuddyLevel;
	}

	public void setActivitiesInvestmentTech(ActivitiesInvestmentTech newActivitiesInvestmentTech) {
		this.activitiesInvestmentTech = newActivitiesInvestmentTech;
	}

	public void setArtBeauty(ArtBeauty newArtBeauty) {
		this.artBeauty = newArtBeauty;
	}

	public void setCareerMajor(CareerMajor newCareerMajor) {
		this.careerMajor = newCareerMajor;
	}

	public void setDecisionMaking(DecisionMaking newDecisionMaking) {
		this.decisionMaking = newDecisionMaking;
	}

	public void setEntertainment(Entertainment newEntertainment) {
		this.entertainment = newEntertainment;
	}

	public void setFood(Food newFood) {
		this.food = newFood;
	}

	public void setGenderType(GenderType newGenderType) {
		this.genderType = newGenderType;
	}

	public void setHobby(Hobby newHobby) {
		this.hobby = newHobby;
	}

	public void setSports(Sports newSports) {
		this.sports = newSports;
	}

	public void setTemperament(Temperament newTemperament) {
		this.temperament = newTemperament;
	}

	public void setWellbeing(Wellbeing newWellbeing) {
		this.wellbeing = newWellbeing;
	}


}
