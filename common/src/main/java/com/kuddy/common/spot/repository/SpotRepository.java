package com.kuddy.common.spot.repository;

import com.kuddy.common.spot.domain.Category;
import com.kuddy.common.spot.domain.District;
import com.kuddy.common.spot.domain.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long>, JpaSpecificationExecutor<Spot> {

    boolean existsByContentId(Long contentId);
    Page<Spot> findAllByCategoryOrderByNumOfHeartsDesc(Category category, Pageable pageable);
    Page<Spot> findAllByDistrictOrderByNumOfHeartsDesc(District district, Pageable pageable);
    Spot findByContentId(Long contentId);
    List<Spot> findTop5ByOrderByNumOfHeartsDesc();
    Page<Spot> findAllByOrderByNumOfHeartsDesc(Pageable pageable);
    List<Spot> findAllByContentIdIn(List<Long> contentIds);

    String HAVERSINE_FORMULA = "(6371 * acos(cos(radians(:mapY)) * cos(radians(s.mapY)) * cos(radians(s.mapX) - radians(:mapX)) + sin(radians(:mapY)) * sin(radians(s.mapY))))";
    @Query(value = "SELECT * FROM spot s WHERE " + HAVERSINE_FORMULA + " <= 2 AND s.category IN ('Attraction', 'Culture', 'Restaurant', 'Festival') ORDER BY "+ HAVERSINE_FORMULA + " ASC", nativeQuery = true)
    List<Spot> findAllByDistance(String mapX, String mapY);

    @Query(value = "SELECT * FROM spot s WHERE " + HAVERSINE_FORMULA + " <= 2 AND s.content_id NOT IN (:contentId) AND s.category IN ('Attraction', 'Culture', 'Restaurant', 'Festival') ORDER BY "+ HAVERSINE_FORMULA + " ASC LIMIT 5", nativeQuery = true)
    List<Spot> findTop5ByDistance(Long contentId, String mapX, String mapY);
}
