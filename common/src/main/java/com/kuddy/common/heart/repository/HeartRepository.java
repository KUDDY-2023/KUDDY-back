package com.kuddy.common.heart.repository;

import com.kuddy.common.heart.domain.Heart;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndSpot(Member member, Spot spot);
    List<Heart> findAllByMemberOrderByIdDesc(Member member);
    Long countByMember(Member member);
    List<Heart> findAllBySpot(Spot spot);
}