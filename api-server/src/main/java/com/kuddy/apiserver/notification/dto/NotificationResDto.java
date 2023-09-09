package com.kuddy.apiserver.notification.dto;

import com.kuddy.common.notification.comment.domain.Notification;
import com.kuddy.common.notification.comment.domain.Notificationtype.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotificationResDto {
    private Long id;
    private String content;
    private Long contentId;
    private Boolean isRead;
    private NotificationType notificationType;
    private LocalDateTime time;

    public static NotificationResDto create(Notification notification){
        return new NotificationResDto(notification.getId(), notification.getContent(), notification.getContentId(), notification.getIsRead(), notification.getNotificationType(), notification.getCreatedDate());
    }
}
