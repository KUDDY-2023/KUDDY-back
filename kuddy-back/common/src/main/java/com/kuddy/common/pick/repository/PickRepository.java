package com.kuddy.common.pick.repository;

import com.kuddy.common.pick.domain.Pick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PickRepository extends JpaRepository<Pick, Long> {
    @Query(value = "SELECT * FROM pick order by RAND() limit 8",nativeQuery = true)
    List<Pick> findAll();
}