package com.kuddy.common.spot.repository;

import com.kuddy.common.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    boolean existsByContentId(Long contentId);
}
