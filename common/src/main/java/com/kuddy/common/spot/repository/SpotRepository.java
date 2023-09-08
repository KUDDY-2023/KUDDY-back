package com.kuddy.common.spot.repository;

import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long>, JpaSpecificationExecutor<Spot> {

    boolean existsByContentId(Long contentId);
    Page<Spot> findAllByCategoryOrderByNumOfHeartsDesc(Category category, Pageable pageable);
    Page<Spot> findAllByDistrictOrderByNumOfHeartsDesc(District district, Pageable pageable);
    Spot findByContentId(Long contentId);
    List<Spot> findTop5ByOrderByNumOfHeartsDesc();
    Page<Spot> findAllByOrderByNumOfHeartsDesc(Pageable pageable);
    List<Spot> findAllByContentIdIn(List<Long> contentIds);
}
