package com.kuddy.apiserver.profile.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.kuddy.apiserver.profile.dto.response.InterestsDto;
import com.kuddy.apiserver.profile.dto.response.MemberAreaDto;
import com.kuddy.apiserver.profile.dto.response.MemberLanguageDto;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.profile.domain.DecisionMaking;
import com.kuddy.common.profile.domain.GenderType;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.Temperament;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileReqDto {

	@Getter
	@AllArgsConstructor
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Create {
		private String job;
		private RoleType roleType;

		@NotBlank(message = "닉네임은 비어있을 수 없습니다.")
		@Size(max = 15, message = "닉네임은 15자를 초과할 수 없습니다.")
		@Pattern(regexp = "[a-zA-Z0-9_]+", message = "닉네임은 영어, 숫자, 언더바만 포함할 수 있습니다.")
		private String nickname;
		private String profileImageUrl;
		private Integer age;
		private String nationality;
		private GenderType genderType;
		private DecisionMaking decisionMaking;
		private Temperament temperament;
		private InterestsDto interests;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;

		public Profile toEntity(Member member){
			return Profile.builder()
				.member(member)
				.age(age)
				.job(job)
				.genderType(genderType)
				.nationality(nationality)
				.temperament(temperament)
				.decisionMaking(decisionMaking)
				.build();
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Update {
		@Size(max = 100, message = "job은 100자를 초과할 수 없습니다.")
		private String job;
		private String introduce;

		private Integer age;
		private String profileImageUrl;

		@NotBlank(message = "닉네임은 비어있을 수 없습니다.")
		@Size(max = 15, message = "닉네임은 15자를 초과할 수 없습니다.")
		@Pattern(regexp = "[a-zA-Z0-9_]+", message = "닉네임은 영어, 숫자, 언더바만 포함할 수 있습니다.")
		private String nickname;
		private String nationality;
		private DecisionMaking decisionMaking;
		private Temperament temperament;
		private GenderType genderType;
		private InterestsDto interests;
		private List<MemberAreaDto> districts;
		private List<MemberLanguageDto> availableLanguages;

	}

}
