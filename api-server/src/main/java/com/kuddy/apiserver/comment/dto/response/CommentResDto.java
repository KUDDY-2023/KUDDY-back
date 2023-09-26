package com.kuddy.apiserver.comment.dto.response;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResDto { //TODO: 유저 레벨 추가
    private Long id;
    private String content;
    private Boolean isRemoved;
    private Boolean isAuthor; //글 작성자인지 여부
    private WriterInfoDto writerInfoDto;
    private LocalDateTime createdDate;
    private List<ReplyResDto> replyList;
    private Long parentId;

    public static CommentResDto of(Comment comment, Post post, Member member, List<ReplyResDto> replyList) {
        WriterInfoDto writerInfoDto = new WriterInfoDto(comment.getWriter().getId(), member.getNickname(), member.getProfileImageUrl(), member.getProfile().getKuddyLevel());
        CommentResDtoBuilder builder = CommentResDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isRemoved(comment.isRemoved())
                .writerInfoDto(writerInfoDto)
                .isAuthor(comment.getWriter().getId().equals(post.getAuthor().getId()))
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
