package com.kuddy.apiserver.member.service;

import static com.kuddy.common.member.domain.Member.*;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.domain.RoleType;
import com.kuddy.common.member.exception.DuplicateNicknameException;
import com.kuddy.common.member.exception.InvalidNicknameException;
import com.kuddy.common.member.exception.MemberNotFoundException;
import com.kuddy.common.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	public Member update(Member member, String nickname, RoleType roleType){
		Member findMember = findById(member.getId());
		findMember.updateNickname(nickname);
		findMember.updateRole(roleType);
		return findMember;
	}

	public void validateNickname(Member member, String nickname) {
		if (Objects.isNull(nickname) || nickname.isBlank() || nickname.length() > 15 || !nickname.matches("[a-zA-Z0-9_]+") || nickname.equalsIgnoreCase(FORBIDDEN_WORD)) {
			throw new InvalidNicknameException();
		}


		if (!member.getNickname().equals(nickname) && isNicknameExists(nickname)) {
			throw new DuplicateNicknameException();
		}
	}

	@Transactional(readOnly = true)
	public boolean isNicknameExists(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	@Transactional(readOnly = true)
	public Member findById(Long memberId){
		return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
	}
}
