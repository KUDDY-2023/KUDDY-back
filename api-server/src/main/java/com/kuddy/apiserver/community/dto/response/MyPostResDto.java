package com.kuddy.apiserver.community.dto.response;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
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
    private Boolean isJoinus;

    public static MyPostResDto of(Post post) {
        return MyPostResDto.builder()
                .id(post.getId())
                .postType(post.getPostType().equals(PostType.ITINERARY)?"itinerary":"talkingBoard")
                .isJoinus(post.getPostType().equals(PostType.JOIN_US))
                .title(post.getTitle())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
