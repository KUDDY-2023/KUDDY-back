package com.kuddy.common.community.repository;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import com.kuddy.common.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPostTypeInOrderByCreatedDateDesc(List<PostType> types, Pageable pageable);

    Page<Post> findAllByPostTypeOrderByCreatedDateDesc(PostType postType, Pageable pageable);

    Page<Post> findAllByAuthorOrderByCreatedDateDesc(Member member, Pageable pageable);
}
