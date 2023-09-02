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
    private Boolean isRemoved;
    private Boolean isAuthor; //글 작성자인지 여부
    private CommentWriterInfoDto commentWriterInfoDto;
    private LocalDateTime createdDate;
    private List<ReplyResDto> replyList;
    private Long parentId;

    public static CommentResDto of(Comment comment, Member member, List<ReplyResDto> replyList) {
        CommentWriterInfoDto commentWriterInfoDto = new CommentWriterInfoDto(comment.getWriter().getId(), member.getNickname(), member.getProfileImageUrl());
        CommentResDtoBuilder builder = CommentResDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isRemoved(comment.isRemoved())
                .isAuthor(comment.getWriter().getId().equals(comment.getPost().getId()))
                .commentWriterInfoDto(commentWriterInfoDto)
                .createdDate(comment.getCreatedDate());

        if (comment.getParent() != null) {
            builder.parentId(comment.getParent().getId());
        }
        if (replyList != null) {
            builder.replyList(replyList);
        }
        CommentResDto commentResDto = builder.build();

        return commentResDto;
    }

}