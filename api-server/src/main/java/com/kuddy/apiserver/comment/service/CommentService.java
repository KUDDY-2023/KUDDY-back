package com.kuddy.apiserver.comment.service;

import com.kuddy.apiserver.comment.dto.request.CommentReqDto;
import com.kuddy.apiserver.comment.dto.request.ReplyReqDto;
import com.kuddy.apiserver.comment.dto.response.CommentDto;
import com.kuddy.apiserver.comment.dto.response.CommentResDto;
import com.kuddy.apiserver.comment.dto.response.MyCommentResDto;
import com.kuddy.apiserver.comment.dto.response.ReplyResDto;
import com.kuddy.apiserver.notification.service.CommentNotiService;
import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.comment.exception.NoAuthorityCommentRemove;
import com.kuddy.common.comment.exception.NoCommentExistsException;
import com.kuddy.common.comment.exception.NoPostExistException;
import com.kuddy.common.comment.repository.CommentQuerydslRepository;
import com.kuddy.common.comment.repository.CommentRepository;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.repository.PostRepository;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.member.repository.MemberRepository;
import com.kuddy.common.notification.comment.domain.Notificationtype.NotificationType;
import com.kuddy.common.response.StatusEnum;
import com.kuddy.common.response.StatusResponse;
import com.kuddy.common.security.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentQuerydslRepository commentQuerydslRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentNotiService notificationService;

    public ResponseEntity<StatusResponse> saveComment(Long postId, CommentReqDto commentReqDto, Member member) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NoPostExistException());
        Comment comment = Comment.builder()
                .content(commentReqDto.getContent())
                .writer(member)
                .post(post)
                .build();
        commentRepository.save(comment);

        // 댓글 작성자가 게시글 작성자와 다를 경우 게시글 작성자에게 알림 생성
        if(!post.getAuthor().getId().equals(member.getId())){
            notificationService.send(post.getAuthor(), NotificationType.COMMENT, commentReqDto.getContent(), comment.getPost().getId());
        }

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(CommentDto.of(comment, post, member, null))
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

        // 대댓글 작성자와 부모 댓글 작성자가 다를 경우 댓글 작성자에게 알림 생성
        if(!parent.getWriter().getId().equals(member.getId())){
            notificationService.send(parent.getWriter(), NotificationType.REPLY, replyReqDto.getContent(), comment.getPost().getId());
        }
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(CommentDto.of(comment, post, member, null))
                .build());
    }

    public ResponseEntity<StatusResponse> getCommentList(Long postId) {
        Post findPost = postRepository.findById(postId).orElseThrow(() -> new NoPostExistException());
        List<Comment> commentList = commentQuerydslRepository.findAllCommentByPost(findPost);
        Integer commentNo = commentRepository.countAllByPostId(postId);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            Member member = memberRepository.findById(comment.getWriter().getId()).get();
            List<Comment> childList = comment.getChildList();
            List<ReplyResDto> replyList = childList.stream()
                    .map(child -> ReplyResDto.of(child, findPost, member))
                    .collect(Collectors.toList());
            CommentDto commentDto = CommentDto.of(comment, findPost, member, replyList);
            commentDtoList.add(commentDto);
        }

        CommentResDto resDto = CommentResDto.builder()
                .commentDtoList(commentDtoList)
                .commentNo(commentNo)
                .build();

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDto)
                .build());

    }

    public ResponseEntity<StatusResponse> getMyCommentList(@AuthUser Member member) {
        List<Comment> myCommentList = commentRepository.findAllByWriterOrderByCreatedDateDesc(member);
        List<MyCommentResDto> resDtos = myCommentList.stream()
                .map(MyCommentResDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .data(resDtos)
                .build());
    }

    public ResponseEntity<StatusResponse> removeComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoCommentExistsException());

        Long writerId = comment.getWriter().getId();
        if (!writerId.equals(member.getId())) {
            throw new NoAuthorityCommentRemove();
        }
        comment.remove();
        List<Comment> commentListToDelete = comment.findCommentListToDelete();
        commentRepository.deleteAll(commentListToDelete);

        return ResponseEntity.ok(StatusResponse.builder()
                .status(StatusEnum.OK.getStatusCode())
                .message(StatusEnum.OK.getCode())
                .build());

    }
}
