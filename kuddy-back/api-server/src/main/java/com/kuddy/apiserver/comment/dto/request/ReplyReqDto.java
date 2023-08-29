package com.kuddy.apiserver.comment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyReqDto {
    private Long parentId;
    private String content;
}
