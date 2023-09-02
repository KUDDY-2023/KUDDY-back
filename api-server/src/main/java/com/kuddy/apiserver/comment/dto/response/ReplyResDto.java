package com.kuddy.apiserver.comment.dto.response;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyResDto {
    private Long id;
    private String content;
    private Long parenttId;
    private Boolean isRemoved;
    private Boolean isAuthor; //글 작성자인지 여부
    private CommentWriterInfoDto commentWriterInfoDto;
    private LocalDateTime createdDate;

    public static ReplyResDto of(Comment child, Long postId, Member member) {
        CommentWriterInfoDto commentWriterInfoDto = new CommentWriterInfoDto(child.getWriter().getId(), member.getNickname(), member.getProfileImageUrl());
        return ReplyResDto.builder()
                .id(child.getId())
                .content(child.getContent())
                .parenttId(child.getParent().getId())
                .isRemoved(child.isRemoved())
                .isAuthor(child.getWriter().getId().equals(postId))
                .commentWriterInfoDto(commentWriterInfoDto)
                .createdDate(child.getCreatedDate())
                .build();

    }
}
