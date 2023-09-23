package com.kuddy.apiserver.notification.dto;

import com.kuddy.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class NotificationPageResDto {
    private List<NotificationResDto> notificationResDtos;
    private PageInfo pageInfo;
}
