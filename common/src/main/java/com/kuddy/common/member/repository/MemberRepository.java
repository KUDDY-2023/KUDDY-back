package com.kuddy.common.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
	Optional<Member> findByNickname(String nickname);

	boolean existsByEmail(String email);

	Optional<Member> findByUsername(String username);

	boolean existsByNickname(String nickname);
}
