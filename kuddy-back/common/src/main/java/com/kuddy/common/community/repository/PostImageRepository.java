package com.kuddy.common.community.repository;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, String> {
    List<PostImage> findAllByPost(Post post);
}
