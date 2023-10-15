package com.kuddy.common.profile.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import com.kuddy.common.member.domain.RoleType;

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

	@Column(length = 100)
	private String job;

	@Column(columnDefinition = "TEXT")
	private String introduce;

	@Column(length = 12)
	private String birthDate;

	@Column(length = 15)
	@Enumerated(EnumType.STRING)
	private KuddyLevel kuddyLevel;

	private String nationality;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private GenderType genderType;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private DecisionMaking decisionMaking;

	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private Temperament temperament;


	@ElementCollection(targetClass = Art.class)
	@CollectionTable(name = "art_beauty_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "art_beauty")
	private List<Art> art = new ArrayList<>();

	@ElementCollection(targetClass = Career.class)
	@CollectionTable(name = "career_major_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "career_major")
	private List<Career> careers = new ArrayList<>();


	@ElementCollection(targetClass = Lifestyle.class)
	@CollectionTable(name = "lifestyle_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "lifestyle")
	private List<Lifestyle> lifestyles = new ArrayList<>();

	@ElementCollection(targetClass = Entertainment.class)
	@CollectionTable(name = "entertainment_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "entertainment")
	private List<Entertainment> entertainments = new ArrayList<>();

	@ElementCollection(targetClass = Food.class)
	@CollectionTable(name = "food_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "food")
	private List<Food> foods = new ArrayList<>();

	@ElementCollection(targetClass = Hobbies.class)
	@CollectionTable(name = "hobbies_interests_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "hobbies_interests")
	private List<Hobbies> hobbies = new ArrayList<>();

	@ElementCollection(targetClass = Sports.class)
	@CollectionTable(name = "sports_mapping", joinColumns = @JoinColumn(name = "profile_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "sports")
	private List<Sports> sports = new ArrayList<>();



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
	public Profile(String job, String birthDate, String nationality,
		DecisionMaking decisionMaking,
		Temperament temperament, GenderType genderType, Member member, String introduce) {
		this.job = job;
		this.birthDate = birthDate;
		this.kuddyLevel = KuddyLevel.NOT_KUDDY;
		this.nationality = nationality;
		this.decisionMaking = decisionMaking;
		this.temperament = temperament;
		this.genderType = genderType;
		this.member = member;
		this.ticketStatus = TicketStatus.NOT_SUBMITTED;
		this.introduce = introduce;
	}

	public void changeJob(String newJob) {
		this.job = newJob;
	}

	public void updateTicketStatus(TicketStatus ticketStatus){
		this.ticketStatus = ticketStatus;
	}
	public void updateIntroduce(String introduce){
		this.introduce = introduce;
	}


	public void setNationality(String newNationality){
		if (!this.nationality.equals(newNationality)) {
			this.nationality = newNationality;
		}
	}


	public void setArtBeauties(List<Art> newArtBeauties) {
		this.art.clear();
		this.art.addAll(newArtBeauties);
	}

	public void setCareers(List<Career> newCareers) {
		this.careers.clear();
		this.careers.addAll(newCareers);
	}


	public void setDecisionMaking(DecisionMaking newDecisionMaking) {
		if (!this.decisionMaking.equals(newDecisionMaking)) {
			this.decisionMaking = newDecisionMaking;
		}
	}

	public void setEntertainments(List<Entertainment> newEntertainments) {
		this.entertainments.clear();
		this.entertainments.addAll(newEntertainments);
	}

	public void setFoods(List<Food> newFoods) {
		this.foods.clear();
		this.foods.addAll(newFoods);
	}

	public void setLifestyles(List<Lifestyle> newLifestyles) {
		this.lifestyles.clear();
		this.lifestyles.addAll(newLifestyles);
	}

	public void setGenderType(GenderType newGenderType) {
		if (!this.genderType.equals(newGenderType)) {
			this.genderType = newGenderType;
		}
	}

	public void setHobbies(List<Hobbies> newHobby) {
		this.hobbies.clear();
		this.hobbies.addAll(newHobby);
	}

	public void setSports(List<Sports> newSports) {
		this.sports.clear();
		this.sports.addAll(newSports);
	}


	public void setTemperament(Temperament newTemperament) {
		if (!Objects.equals(this.temperament, newTemperament)) {
			this.temperament = newTemperament;
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
	public void updateBirthDate(String birthDate){
		this.birthDate= birthDate;
	}

	public void initKuddyLevel(RoleType roleType){
		if (roleType == RoleType.KUDDY) {
			this.kuddyLevel = KuddyLevel.EXPLORER;
		}
	}
	public void updateKuddyLevelByMeetup(long meetNum, RoleType roleType) {
		KuddyLevel newKuddyLevel;

		if (meetNum < 4) {
			newKuddyLevel = KuddyLevel.of((int)(meetNum + 1));
		} else {
			newKuddyLevel = KuddyLevel.SOULMATE;
		}

		if (roleType == RoleType.KUDDY && newKuddyLevel != null && this.kuddyLevel != newKuddyLevel) {
			this.kuddyLevel = newKuddyLevel;
		}
	}


}
