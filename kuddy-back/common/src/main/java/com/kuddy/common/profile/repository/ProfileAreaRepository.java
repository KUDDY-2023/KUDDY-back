package com.kuddy.common.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.profile.domain.Area;
import com.kuddy.common.profile.domain.Profile;
import com.kuddy.common.profile.domain.ProfileArea;

public interface ProfileAreaRepository extends JpaRepository<ProfileArea, Long> {
	Optional<ProfileArea> findByProfileAndArea(Profile profile, Area area);
}
