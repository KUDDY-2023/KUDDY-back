package com.kuddy.apiserver.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentWriterInfoDto {
    private Long writerId;
    private String username;
    private String profileImageUrl;
}
