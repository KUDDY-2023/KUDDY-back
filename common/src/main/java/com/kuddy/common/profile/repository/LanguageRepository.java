package com.kuddy.common.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.profile.domain.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
	Optional<Language> findByType(String type);
}
