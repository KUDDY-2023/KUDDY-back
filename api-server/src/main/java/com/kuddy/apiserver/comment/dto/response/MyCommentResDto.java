package com.kuddy.apiserver.comment.dto.response;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyCommentResDto {
    private Long id;
    private String postType;
    private String postTitle;
    private LocalDateTime createdDate;
    private Boolean isJoinus;

    public static MyCommentResDto of(Comment comment) {
        Post post = comment.getPost();
        return MyCommentResDto.builder()
                .id(comment.getId())
                .postType(post.getPostType().equals(PostType.ITINERARY)?"itinerary":"talkingBoard")
                .isJoinus(post.getPostType().equals(PostType.JOIN_US))
                .postTitle(post.getTitle())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
