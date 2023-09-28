package com.kuddy.apiserver.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CommentResDto {
    private List<CommentDto> commentDtoList;
    private Integer commentNo;
}
