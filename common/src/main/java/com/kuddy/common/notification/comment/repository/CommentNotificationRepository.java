package com.kuddy.common.notification.comment.repository;

import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.comment.domain.Notification;
import com.kuddy.common.notification.comment.domain.Notificationtype.NotificationType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentNotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiverIdAndNotificationTypeIn(Long receiverId, List<NotificationType> types, Pageable pageable);
    List<Notification> findAllByReceiverIdAndNotificationTypeIn(Long receiverId, List<NotificationType> types);


    long countByIsReadFalseAndReceiverAndNotificationTypeIn(Member receiver, List<NotificationType> types);
}
