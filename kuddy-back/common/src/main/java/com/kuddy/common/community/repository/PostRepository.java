package com.kuddy.common.community.repository;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPostTypeIn(List<PostType> types, Pageable pageable);

    Page<Post> findAllByPostType(PostType postType, Pageable pageable);
}
