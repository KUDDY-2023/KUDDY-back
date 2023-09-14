package com.kuddy.common.notification.comment.domain;

import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.notification.comment.domain.Notificationtype.NotificationType;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", updatable = false)
    private Long id;

    private String content;

    @Column(name = "is_read")
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private Long contentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member receiver;

    @Builder
    public Notification(String content, Boolean isRead, NotificationType notificationType, Long contentId, Member receiver) {
        this.content = content;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.contentId = contentId;
        this.receiver = receiver;
    }

    public void read(){
        isRead = true;
    }
}
