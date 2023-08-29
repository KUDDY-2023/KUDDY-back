package com.kuddy.common.comment.repository;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

}
