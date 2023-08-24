package com.kuddy.common.exception.type;

import java.util.Arrays;
import java.util.Objects;

import com.kuddy.common.exception.custom.ApplicationException;
import com.kuddy.common.security.exception.EmptyTokenException;
import com.kuddy.common.security.exception.InvalidAccessTokenAtRenewException;
import com.kuddy.common.security.exception.ExpiredTokenException;
import com.kuddy.common.security.exception.InvalidRefreshTokenException;
import com.kuddy.common.security.exception.InvalidTokenException;
import com.kuddy.common.security.exception.InvalidTokenTypeException;
import com.kuddy.common.security.exception.UnAuthorizedTokenException;

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
	TOUR_API_EXCEPTION("C4000", "TourApi를 불러올 수 없습니다.", TourApiExeption.class);

	//회원 관련 - C2***
	//프로필 관련 - C3***

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