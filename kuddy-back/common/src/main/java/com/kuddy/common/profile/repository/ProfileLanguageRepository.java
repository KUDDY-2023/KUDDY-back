package com.kuddy.common.profile.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.profile.domain.Language;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileLanguage;

public interface ProfileLanguageRepository extends JpaRepository<ProfileLanguage, Long> {

	Optional<ProfileLanguage> findByProfileAndLanguage(Profile profile, Language language);
}
