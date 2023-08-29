package com.kuddy.apiserver.comment.controller;

import com.kuddy.apiserver.comment.dto.request.CommentReqDto;
import com.kuddy.apiserver.comment.dto.request.ReplyReqDto;
import com.kuddy.apiserver.comment.service.CommentService;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<StatusResponse> saveComment(@PathVariable("postId") Long postId, @RequestBody CommentReqDto commentReqDto, @AuthUser Member member) {
        return commentService.saveComment(postId, commentReqDto, member);
    }

    @PostMapping("/reply/{postId}")
    public ResponseEntity<StatusResponse> saveReply(@PathVariable("postId") Long postId, @RequestBody ReplyReqDto replyReqDto, @AuthUser Member member) {
        return commentService.saveReply(postId, replyReqDto, member);
    }
//
//    @GetMapping("/{postId}")
//    public ResponseEntity<StatusResponse> getCommentList(@PathVariable("postId") Long postId) {
//
//    }


}
