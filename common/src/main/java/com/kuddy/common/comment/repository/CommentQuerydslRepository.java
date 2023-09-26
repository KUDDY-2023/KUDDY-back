package com.kuddy.common.comment.repository;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.community.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.kuddy.common.comment.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentQuerydslRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Comment> findAllCommentByPost(Post post){
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        return jpaQueryFactory
                .selectFrom(comment)
                .where(comment.post.id.eq(post.getId())
                        .and(comment.parent.id.isNull()))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }
}
