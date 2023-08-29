package com.kuddy.common.profile.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;

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

	@Column(length = 100)
	private Integer age;

	private Integer kuddyLevel;

	private String nationality;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private DecisionMaking decisionMaking;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Temperament temperament;

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
	private Lifestyle lifestyle;
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
	private HobbiesInterests hobbiesInterests;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Sports sports;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Wellbeing wellbeing;


	@OneToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
	private List<ProfileLanguage> availableLanguages = new ArrayList<>();

	@OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
	private List<ProfileArea> districts;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private TicketStatus ticketStatus;

	@Builder
	public Profile(String job, Integer age, Integer kuddyLevel, String nationality,
		DecisionMaking decisionMaking,
		Temperament temperament, ActivitiesInvestmentTech activitiesInvestmentTech, ArtBeauty artBeauty,
		CareerMajor careerMajor, Lifestyle lifestyle, Entertainment entertainment, Food food, GenderType genderType,
		HobbiesInterests hobbiesInterests, Sports sports, Wellbeing wellbeing, List<ProfileArea> districts, Member member,
		List<ProfileLanguage> availableLanguages, TicketStatus ticketStatus) {
		this.job = job;
		this.age = age;
		this.kuddyLevel = 0;
		this.nationality = nationality;
		this.decisionMaking = decisionMaking;
		this.temperament = temperament;
		this.activitiesInvestmentTech = activitiesInvestmentTech;
		this.artBeauty = artBeauty;
		this.careerMajor = careerMajor;
		this.lifestyle = lifestyle;
		this.entertainment = entertainment;
		this.food = food;
		this.genderType = genderType;
		this.hobbiesInterests = hobbiesInterests;
		this.sports = sports;
		this.wellbeing = wellbeing;
		this.districts = districts;
		this.member = member;
		this.availableLanguages = availableLanguages;
		this.ticketStatus = TicketStatus.NOT_SUBMITTED;
	}

	public void changeJob(String newJob) {
		this.job = newJob;
	}

	public void updateKuddyLevel(Integer newKuddyLevel) {
		this.kuddyLevel = newKuddyLevel;
	}
	public void updateTicketStatus(TicketStatus ticketStatus){
		this.ticketStatus = ticketStatus;
	}


	public void setNationality(String newNationality){
		if (!Objects.equals(this.nationality, newNationality)) {
			this.nationality = newNationality;
		}
	}

	public void setActivitiesInvestmentTech(ActivitiesInvestmentTech newActivitiesInvestmentTech) {
		if (!Objects.equals(this.activitiesInvestmentTech, newActivitiesInvestmentTech)) {
			this.activitiesInvestmentTech = newActivitiesInvestmentTech;
		}
	}

	public void setArtBeauty(ArtBeauty newArtBeauty) {
		if (!Objects.equals(this.artBeauty, newArtBeauty)) {
			this.artBeauty = newArtBeauty;
		}
	}
	public void setCareerMajor(CareerMajor newCareerMajor) {
		if (!Objects.equals(this.careerMajor, newCareerMajor)) {
			this.careerMajor = newCareerMajor;
		}
	}

	public void setDecisionMaking(DecisionMaking newDecisionMaking) {
		if (!Objects.equals(this.decisionMaking, newDecisionMaking)) {
			this.decisionMaking = newDecisionMaking;
		}
	}

	public void setEntertainment(Entertainment newEntertainment) {
		if (!Objects.equals(this.entertainment, newEntertainment)) {
			this.entertainment = newEntertainment;
		}
	}

	public void setFood(Food newFood) {
		if (!Objects.equals(this.food, newFood)) {
			this.food = newFood;
		}
	}
	public void setLifestyle(Lifestyle newLifestyle) {
		if (!Objects.equals(this.lifestyle, newLifestyle)) {
			this.lifestyle = newLifestyle;
		}
	}

	public void setGenderType(GenderType newGenderType) {
		if (!Objects.equals(this.genderType, newGenderType)) {
			this.genderType = newGenderType;
		}
	}

	public void setHobby(HobbiesInterests newHobby) {
		if (!Objects.equals(this.hobbiesInterests, newHobby)) {
			this.hobbiesInterests = newHobby;
		}
	}

	public void setSports(Sports newSports) {
		if (!Objects.equals(this.sports, newSports)) {
			this.sports = newSports;
		}
	}

	public void setTemperament(Temperament newTemperament) {
		if (!Objects.equals(this.temperament, newTemperament)) {
			this.temperament = newTemperament;
		}
	}

	public void setWellbeing(Wellbeing newWellbeing) {
		if (!Objects.equals(this.wellbeing, newWellbeing)) {
			this.wellbeing = newWellbeing;
		}
	}
	public void updateAvailableLanguages(ProfileLanguage newAvailableLanguage) {
		if (!this.availableLanguages.contains(newAvailableLanguage)) {
			this.availableLanguages.add(newAvailableLanguage);
		}
	}
	public void setAvailableLanguages(List<ProfileLanguage> profileLanguages){
		this.availableLanguages = profileLanguages;
	}
	public void updateDistricts(ProfileArea newDistrict){
		if(!this.districts.contains(newDistrict)){
			this.districts.add(newDistrict);
		}
	}

	public void setDistricts(List<ProfileArea> districts) {
		this.districts = districts;
	}
	public void updateAge(Integer age){
		this.age = age;
	}

}
