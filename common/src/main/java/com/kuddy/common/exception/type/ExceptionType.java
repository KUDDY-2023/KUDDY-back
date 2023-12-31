package com.kuddy.common.exception.type;


import java.util.Arrays;
import java.util.Objects;

import com.kuddy.common.chat.exception.*;

import com.kuddy.common.comment.exception.NoAuthorityCommentRemove;
import com.kuddy.common.comment.exception.NoCommentExistsException;
import com.kuddy.common.comment.exception.NoPostExistException;
import com.kuddy.common.community.exception.EmptyInputFilenameException;
import com.kuddy.common.community.exception.ExpiredDateException;
import com.kuddy.common.community.exception.InvalidPostArgumentsException;
import com.kuddy.common.community.exception.NoAuthorityPostRemove;
import com.kuddy.common.community.exception.NoDistrictExistsException;
import com.kuddy.common.community.exception.WrongImageFormatException;
import com.kuddy.common.exception.custom.ApplicationException;
import com.kuddy.common.meetup.exception.NotAdminException;
import com.kuddy.common.notification.exception.GoogleCalendarAPIException;
import com.kuddy.common.notification.exception.KakaoCalendarAPIException;
import com.kuddy.common.notification.exception.KakaoCalendarAPIPermissionException;
import com.kuddy.common.notification.exception.NotificationNotFoundException;
import com.kuddy.common.profile.exception.DuplicateProfileException;
import com.kuddy.common.profile.exception.WithdrawMemberProfileException;
import com.kuddy.common.review.exception.NoAuthorityToDeleteReview;
import com.kuddy.common.review.exception.NotKuddyException;
import com.kuddy.common.review.exception.NotTravelerException;
import com.kuddy.common.review.exception.ReviewNotFoundException;
import com.kuddy.common.security.exception.AlreadyLogoutException;
import com.kuddy.common.security.exception.NotMatchStoredResfreshTokenException;
import com.kuddy.common.spot.exception.*;
import com.kuddy.common.heart.exception.AlreadyLikedException;
import com.kuddy.common.heart.exception.HeartNotFoundException;
import com.kuddy.common.meetup.exception.MeetupNotFoundException;
import com.kuddy.common.member.exception.DuplicateNicknameException;
import com.kuddy.common.member.exception.InvalidNicknameException;

import com.kuddy.common.pick.exception.PickNotFoundException;

import com.kuddy.common.member.exception.NotAuthorException;
import com.kuddy.common.profile.exception.AreaNotFoundException;
import com.kuddy.common.profile.exception.LanguageNotFoundException;
import com.kuddy.common.profile.exception.ProfileNotFoundException;
import com.kuddy.common.report.exception.ReportNotFoundException;
import com.kuddy.common.security.exception.EmptyTokenException;
import com.kuddy.common.security.exception.ExpiredTokenException;
import com.kuddy.common.security.exception.InvalidRefreshTokenException;
import com.kuddy.common.security.exception.InvalidTokenException;
import com.kuddy.common.security.exception.InvalidTokenTypeException;
import com.kuddy.common.security.exception.UnAuthorizedTokenException;
import com.kuddy.common.ticket.exception.OnlyTravelerRequestException;
import com.kuddy.common.ticket.exception.TicketNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionType {

	// 서버 자체 관련 - C0***
	UNHANDLED_EXCEPTION("C0000", "알 수 없는 서버 에러가 발생했습니다."),
	METHOD_ARGUMENT_NOT_VALID_EXCEPTION("C0001", "요청 데이터가 잘못되었습니다."),

	//권한 관련 - C1***
  
	EMPTY_TOKEN_EXCEPTION("C1003", "토큰이 존재하지 않습니다.", EmptyTokenException.class),
	INVALID_TOKEN_TYPE_EXCEPTION("C1004", "토큰 타입이 올바르지 않습니다.", InvalidTokenTypeException .class),
	EXPIRED_TOKEN_EXCEPTION("C1005", "토큰이 유효하지 않습니다.", ExpiredTokenException.class),
	NOT_AUTHOR_EXCEPTION("C1007", "작성자가 아니므로 권한이 없습니다.", NotAuthorException.class),
	NOT_ADMIN_EXCEPTION("C1008", "관리자가 아니므로 권한이 없습니다.", NotAdminException.class),

	INVALID_REFRESH_TOKEN_EXCEPTION("C1009", "리프레시 토큰이 유효하지 않습니다.", InvalidRefreshTokenException.class),
	UNAUTHORIZED_TOKEN_EXCEPTION("C1010", "권한이 없는 토큰입니다.", UnAuthorizedTokenException.class),
	NO_MATCH_REFRESHTOKEN_EXCEPTION("C1011", "해당 유저의 저장된 리프레시 토큰과 일치하지 않습니다.",
		NotMatchStoredResfreshTokenException.class),
	INVALID_TOKEN_EXCEPTION("C1012", "토큰이 유효하지 않습니다.", InvalidTokenException.class),
	ALREADY_LOGOUT_EXCEPTION("C1013", "이미 로그아웃된 사용자 입니다.", AlreadyLogoutException.class),


	//관광지 관련 - C4***
	TOUR_API_EXCEPTION("C4000", "TourApi를 불러올 수 없습니다.", TourApiExeption.class),
	SPOT_NOT_FOUND_EXCEPTION("C4001", "해당 관광지 정보를 찾을 수 없습니다.",SpotNotFoundException.class),
	HEART_NOT_FOUND_EXCEPTION("C4002", "해당 관광지에 대한 찜 정보를 찾을 수 없습니다.", HeartNotFoundException.class),
	ALREADY_LIKED_EXCEPTION("C4003", "이미 찜한 관광지입니다.", AlreadyLikedException.class),
	NO_SPOT_EXISTS_EXCEPTION("C4004", "존재하지 않는 spot id입니다.", NoSpotExists.class),
	NO_SPOT_NEARBY_EXCEPTION("C4005", "주변에 관광지가 없습니다.", NoSpotNearbyException.class),
	SPOT_RECOMMEND_API_EXCEPTION("C4006", "장소 추천 API를 불러올 수 없습니다.", SpotRecommendApiException.class),

	//회원 관련 - C2***
	INVALID_NICKNAME_EXCEPTION("C2000", "유효하지 않은 닉네임입니다.", InvalidNicknameException.class),
	DUPLICATE_NICKNAME_EXCEPTION("C2001", "중복된 닉네임이 존재합니다.", DuplicateNicknameException.class),
	//프로필 관련 - C3***
	PROFILE_NOT_FOUND_EXCEPTION("C3000", "프로필을 찾을 수 없습니다.", ProfileNotFoundException.class),
	LANGUAGE_NOT_FOUND_EXCEPTION("C3001", "해당 언어를 찾을 수 없습니다.", LanguageNotFoundException.class),
	AREA_NOT_FOUND_EXCEPTION("C3002", "해당 지역을 찾을 수 없습니다.", AreaNotFoundException.class),
	DUPLICATED_PROFILE_EXCEPTION("C3003", "이미 생성된 프로필이 존재하므로 다시 생성할 수 없습니다.", DuplicateProfileException.class),
	WITHDRAW_MEMBER_PROFILE_EXCEPTION("C3004", "탈퇴한 회원의 프로필입니다.", WithdrawMemberProfileException.class),
	//게시글 관련 - C5***
	EXPIRED_DATE_EXCEPTION("C5000", "이미 지난 날짜를 동행 날짜로 설정할 수 없습니다.", ExpiredDateException.class),
	EMPTY_INPUT_FILENAME_EXCEPTION("C5001", "이미지 파일명은 NULL값을 가질 수 없습니다.", EmptyInputFilenameException.class),
	WRONG_IMAGE_FORMAT_EXCEPTION("C5002", "이미지 파일 확장자는 jpg(JPG), png(PNG), jpeg(JPEG)만 가능합니다.", WrongImageFormatException.class),
	NO_DISTRICT_EXISTS_EXCEPTION("C5003", "유효하지 않은 지역구입니다.", NoDistrictExistsException.class),
	INVALID_ARGUMENTS_FOR_POST_EXCEPTION("C5004", "게시글 타입에 맞지 않는 인자입니다.", InvalidPostArgumentsException.class),
	NO_POST_EXIST_EXCEPTION("C5005", "존재하지 않는 게시글입니다.", NoPostExistException.class),
	NO_AUTHORITY_POST_REMOVE("C5006", "본인이 작성한 게시글만 삭제할 수 있습니다.", NoAuthorityPostRemove.class),
	
	//댓글 관련 - C6***
	NO_COMMENT_EXIST_EXCEPTION("C6001", "존재하지 않는 댓글입니다.", NoCommentExistsException.class),
	NO_AUTHORITY_COMMENT_REMOVE("C6002", "본인이 작성한 댓글만 삭제할 수 있습니다.", NoAuthorityCommentRemove.class),

	//채팅 관련 - C7***
	CHAT_ROOM_NOT_FOUND_EXCEPTION("C7000", "해당 접속한 채팅방을 찾을 수 없습니다.", ChatRoomNotFoundException.class),
	ROOM_NOT_FOUND_EXCEPTION("C7001", "해당 채팅방을 찾을 수 없습니다.", RoomNotFoundException.class),
	CHAT_NOT_FOUND_EXCEPTION("C7002", "해당 채팅을 찾을 수 없습니다.", ChatNotFoundException.class),
	NOT_CHATROOM_OWNER_EXCEPTION("C7003", "해당 roomId의 주인이 아니므로 권한이 없습니다.", NotChatRoomOwnerException.class),
	NOT_MATCH_LOGINMEMBER_EXCEPTION("C7004", "해당 email과 로그인한 유저의 email이 일치하지 않습니다.", NotMatchLoginMemberEmailException.class),
	CHATROOM_ALREADY_EXISTS_EXCEPTION("C7005", "이미 해당 회원과 만들어진 채팅방이 있습니다.", ChatRoomAlreadyExistsException.class),
	NO_ROOM_EXISTS_EXCEPTION("C7006", "생성된 채팅방이 없습니다.", NoRoomExistsException.class),
	//신고 관련 - C9***
	REPORT_NOT_FOUND_EXCEPTION("C9000", "해당 신고 기록을 찾을 수 없습니다.", ReportNotFoundException.class),



	//커디스픽 관련 - C10***
	PICK_NOT_FOUND_EXCEPTION("C10000", "해당 커디스픽 정보를 찾을 수 없습니다.", PickNotFoundException.class),


	//meetup 관련 - C8***
	MEETUP_NOT_FOUND_EXCEPTION("C8000", "해당 meetup을 찾을 수 없습니다.", MeetupNotFoundException.class),
	//리뷰 관련 - C11***
	REVIEW_NOT_FOUND_EXCEPTION("C11000", "해당 리뷰 정보를 찾을 수 없습니다.", ReviewNotFoundException.class),
	NO_AUTHORITY_TO_DELETE_REVIEW("C11001", "본인이 작성한 리뷰만 삭제할 수 있습니다.", NoAuthorityToDeleteReview.class),
	NOT_KUDDY_EXCEPTION("C11002", "해당 Member가 Kuddy인 경우에만 조회 가능합니다", NotKuddyException.class),
	NOT_TRAVELER_EXCEPTION("C11002", "해당 Member가 Traveler인 경우에만 조회 가능합니다", NotTravelerException.class),

	//티켓 관련 - C12***
	TICKET_NOT_FOUNED_EXCEPTION("C12000", "티켓 정보를 찾을 수 없습니다.", TicketNotFoundException.class),
	ONLY_TRAVELER_REQUEST_EXCEPTION("C12001", "Traveler만 가능한 요청입니다.", OnlyTravelerRequestException.class),

	//알림 관련 - C13***
	GOOGLE_CALENDAR_API_EXCEPTION("C13000", "Google Calendar API를 불러올 수 없습니다.", GoogleCalendarAPIException.class),
	KAKAO_CALENDAR_API_EXCEPTION("C13001", "Kakao 톡캘린더 API를 불러올 수 없습니다.", KakaoCalendarAPIException.class),
	NOTIFICATION_NOT_FOUND_EXCEPTION("C13002", "해당 알림을 찾을 수 없습니다.", NotificationNotFoundException.class),
	KAKAO_CALENDAR_API_PERMISSION_EXCEPTION("C13003", "톡캘린더 권한 동의가 필요합니다.", KakaoCalendarAPIPermissionException.class);

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
