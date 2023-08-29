package com.kuddy.apiserver.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyResDto {
    private Long id;
    private String content;
    private boolean isDeleted;
    private Boolean isAuthor; //글 작성자인지 여부
    private CommentWriterInfoDto commentWriterInfoDto;
    private LocalDateTime createdDate;
}
