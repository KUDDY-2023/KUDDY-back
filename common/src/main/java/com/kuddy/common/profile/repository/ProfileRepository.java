package com.kuddy.common.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	Optional<Profile> findByMember(Member member);
	Optional<Profile> findById(Long id);
	boolean existsByMember(Member member);
}
