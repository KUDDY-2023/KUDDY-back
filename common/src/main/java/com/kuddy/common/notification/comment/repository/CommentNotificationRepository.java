package com.kuddy.common.notification.comment.repository;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.comment.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentNotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<Notification> findAllByReceiverId(Long receiverId);

    long countByIsReadFalseAndReceiver(Member member);
}
