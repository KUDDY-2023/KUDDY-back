package com.kuddy.apiserver.community.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyPostResDto {
    private Long id;
    private String postType;
    private String title;
    private LocalDateTime createdDate;

    @Builder
    public MyPostResDto(Long id, String postType, String title, LocalDateTime createdDate) {
        this.id = id;
        this.postType = postType;
        this.title = title;
        this.createdDate = createdDate;
    }
}
