package com.kuddy.apiserver.member.controller;

import static org.springframework.http.HttpHeaders.*;


import javax.validation.Valid;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuddy.apiserver.auth.dto.AccessTokenDto;
import com.kuddy.apiserver.auth.service.AuthService;
import com.kuddy.apiserver.member.dto.MemberResDto;
import com.kuddy.apiserver.member.service.MemberService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;
	private final AuthService authService;
	private static final String CHECK_NICKNAME_SUCCESS = "사용가능한 닉네임입니다.";
	private static final String WITHDRAW_SUCCESS = "성공적으로 회원 탈퇴가 완료되었습니다.";
	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> readMember(@AuthUser Member member) {
		Member findMember = memberService.findById(member.getId());
		MemberResDto response = MemberResDto.of(findMember);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(response)
			.build());
	}
	@GetMapping("/nickname")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<StatusResponse> validateNickname(@AuthUser Member member, @RequestParam final String nickname) {
		memberService.validateNickname(member, nickname);
		return ResponseEntity.ok(StatusResponse.builder()
			.status(StatusEnum.OK.getStatusCode())
			.message(StatusEnum.OK.getCode())
			.data(CHECK_NICKNAME_SUCCESS)
			.build());
	}
	@DeleteMapping
	public ResponseEntity<StatusResponse> withdrawMember(@Valid @RequestBody final AccessTokenDto requestDto){
		Authentication authentication = memberService.withdraw(requestDto);
		authService.withdrawAuthInfo(authentication, requestDto.getAccessToken());
		ResponseCookie responseCookie = authService.removeRefreshTokenCookie();
		return ResponseEntity.ok()
			.header(SET_COOKIE, responseCookie.toString())
			.body(StatusResponse.builder()
				.status(StatusEnum.OK.getStatusCode())
				.message(StatusEnum.OK.getCode())
				.data(WITHDRAW_SUCCESS)
				.build());
	}

}
