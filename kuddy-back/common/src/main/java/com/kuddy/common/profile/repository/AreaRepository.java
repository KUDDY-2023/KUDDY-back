package com.kuddy.common.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kuddy.common.profile.domain.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
	Optional<Area> findByDistrict(String district);
}
