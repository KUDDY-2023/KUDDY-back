package com.kuddy.apiserver.comment.dto.response;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResDto {
    private Long id;
    private String content;
    private boolean isDeleted;
    private Boolean isAuthor; //글 작성자인지 여부
    private CommentWriterInfoDto commentWriterInfoDto;
    private LocalDateTime createdDate;
    private List<ReplyResDto> replyList;
    private Long parentId;

    public static CommentResDto of(Comment comment, Member member, List<ReplyResDto> replyList) {
        CommentWriterInfoDto commentWriterInfoDto = new CommentWriterInfoDto(comment.getWriter().getId(), member.getUsername(), member.getProfileImageUrl());
        CommentResDtoBuilder builder = CommentResDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .isAuthor(comment.getWriter().getId().equals(member.getId()))
                .commentWriterInfoDto(commentWriterInfoDto)
                .createdDate(comment.getCreatedDate());

        if (comment.getParent() != null) {
            builder.parentId(comment.getParent().getId());
        }
        CommentResDto commentResDto = builder.build();

        return commentResDto;
    }

}
