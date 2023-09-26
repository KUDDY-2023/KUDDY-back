package com.kuddy.common.review.repository;

import com.kuddy.common.review.domain.Grade;
import com.kuddy.common.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByMeetupKuddyIdOrderByCreatedDateDesc(Long id);
    Optional<Review> findFirstByMeetupKuddyIdOrderByCreatedDateDesc(Long kuddyId);
    Long countByMeetupKuddyId(Long id);
    Long countByMeetupKuddyIdAndGrade(Long id, Grade grade);

    List<Review> findAllByWriterIdOrderByCreatedDateDesc(Long id);
    Long countByWriterId(Long id);
}
