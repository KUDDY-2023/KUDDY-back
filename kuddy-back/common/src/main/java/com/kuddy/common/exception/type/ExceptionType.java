package com.kuddy.common.exception.type;

import java.util.Arrays;
import java.util.Objects;

import com.kuddy.common.exception.custom.ApplicationException;
import com.kuddy.common.heart.exception.AlreadyLikedException;
import com.kuddy.common.heart.exception.HeartNotFoundException;
import com.kuddy.common.member.exception.DuplicateNicknameException;
import com.kuddy.common.member.exception.InvalidNicknameException;
import com.kuddy.common.profile.domain.Language;
import com.kuddy.common.profile.exception.AreaNotFoundException;
import com.kuddy.common.profile.exception.LanguageNotFoundException;
import com.kuddy.common.profile.exception.ProfileNotFoundException;
import com.kuddy.common.security.exception.EmptyTokenException;
import com.kuddy.common.security.exception.InvalidAccessTokenAtRenewException;
import com.kuddy.common.security.exception.ExpiredTokenException;
import com.kuddy.common.security.exception.InvalidRefreshTokenException;
import com.kuddy.common.security.exception.InvalidTokenException;
import com.kuddy.common.security.exception.InvalidTokenTypeException;
import com.kuddy.common.security.exception.UnAuthorizedTokenException;

import com.kuddy.common.spot.exception.SpotNotFoundException;
import com.kuddy.common.spot.exception.TourApiExeption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionType {

	// 서버 자체 관련 - C0***
	UNHANDLED_EXCEPTION("C0000", "알 수 없는 서버 에러가 발생했습니다."),
	METHOD_ARGUMENT_NOT_VALID_EXCEPTION("C0001", "요청 데이터가 잘못되었습니다."),


	//권한 관련 - C1***

	EMPTY_TOKEN_EXCEPTION("C1003", "토큰이 존재하지 않습니다.", EmptyTokenException .class),
	INVALID_TOKEN_TYPE_EXCEPTION("C1004", "토큰 타입이 올바르지 않습니다.", InvalidTokenTypeException .class),
	EXPIRED_TOKEN_EXCEPTION("C1005", "토큰이 유효하지 않습니다.", ExpiredTokenException.class),
	/*HTTP_REQUEST_NULL_EXCEPTION("C1006", "인증할 수 있는 사용자 데이터가 없습니다.", HttpRequestNullException.class),
	NOT_AUTHOR_EXCEPTION("C1007", "작성자가 아니므로 권한이 없습니다.", NotAuthorException.class),
	NOT_MEMBER_EXCEPTION("C1008", "회원이 아니므로 권한이 없습니다.", NotMemberException.class),

	 */
	INVALID_REFRESH_TOKEN_EXCEPTION("C1009", "리프레시 토큰이 유효하지 않습니다.", InvalidRefreshTokenException.class),
	UNAUTHORIZED_TOKEN_EXCEPTION("C1010", "유효한 액세스 토큰으로 리프레시 토큰을 발급할 수 없습니다.", UnAuthorizedTokenException.class),
	INVALID_ACCESS_TOKEN_AT_RENEW_EXCEPTION("C1011", "유효하지 않는 액세스 토큰으로 권한이 없는 유저입니다. 재로그인을 해주세요",
		InvalidAccessTokenAtRenewException.class),
	INVALID_TOKEN_EXCEPTION("C1012", "토큰이 유효하지 않습니다.", InvalidTokenException.class),


	//관광지 관련 - C4***
	TOUR_API_EXCEPTION("C4000", "TourApi를 불러올 수 없습니다.", TourApiExeption.class),
	TOUR_API_EXCEPTION("C4000", "TourApi를 불러올 수 없습니다.", TourApiExeption.class),
	SPOT_NOT_FOUND_EXCEPTION("C4001", "해당 관광지 정보를 찾을 수 없습니다.",SpotNotFoundException.class),
	HEART_NOT_FOUND_EXCEPTION("C4002", "해당 관광지에 대한 찜 정보를 찾을 수 없습니다.", HeartNotFoundException.class),
	ALREADY_LIKED_EXCEPTION("C4003", "이미 찜한 관광지입니다.", AlreadyLikedException.class);

	//회원 관련 - C2***
	INVALID_NICKNAME_EXCEPTION("C2000", "유효하지 않은 닉네임입니다.", InvalidNicknameException.class),
	DUPLICATE_NICKNAME_EXCEPTION("C2001", "중복된 닉네임이 존재합니다.", DuplicateNicknameException.class),
	//프로필 관련 - C3***
	PROFILE_NOT_FOUND_EXCEPTION("C3000", "프로필을 찾을 수 없습니다.", ProfileNotFoundException.class),
	LANGUEAGE_NOT_FOUND_EXCEPTION("C3001", "해당 언어를 찾을 수 없습니다.", LanguageNotFoundException.class),
	AREA_NOT_FOUND_EXCEPTION("C3002", "해당 지역을 찾을 수 없습니다.", AreaNotFoundException.class);

	//게시글 관련 - C5***
	//댓글 관련 - C6***


	private final String errorCode;
	private final String message;
	private Class<? extends ApplicationException> type;

	ExceptionType(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public static ExceptionType from(Class<?> classType) {
		return Arrays.stream(values())
			.filter(it -> Objects.nonNull(it.type) && it.type.equals(classType))
			.findFirst()
			.orElse(UNHANDLED_EXCEPTION);
	}
}
