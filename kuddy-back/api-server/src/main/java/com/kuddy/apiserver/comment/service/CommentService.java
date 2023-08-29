package com.kuddy.apiserver.comment.service;

import com.kuddy.apiserver.comment.dto.request.CommentReqDto;
import com.kuddy.apiserver.comment.dto.request.ReplyReqDto;
import com.kuddy.apiserver.comment.dto.response.CommentResDto;
import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.comment.exception.NoCommentExistsException;
import com.kuddy.common.comment.exception.NoPostExistException;
import com.kuddy.common.comment.repository.CommentRepository;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.repository.PostRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity<StatusResponse> saveComment(Long postId, CommentReqDto commentReqDto, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostExistException());
        Comment comment = Comment.builder()
                .content(commentReqDto.getContent())
                .writer(member)
                .post(post)
                .build();

        commentRepository.save(comment);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(CommentResDto.of(comment, member, null))
                .build());
    }

    public ResponseEntity<StatusResponse> saveReply(Long postId, ReplyReqDto replyReqDto, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostExistException());
        Comment parent = commentRepository.findById(replyReqDto.getParentId())
                .orElseThrow(() -> new NoCommentExistsException());
        Comment comment = Comment.builder()
                .content(replyReqDto.getContent())
                .writer(member)
                .post(post)
                .build();
        comment.setParent(parent);
        commentRepository.save(comment);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(CommentResDto.of(comment, member, null))
                .build());
    }
}
