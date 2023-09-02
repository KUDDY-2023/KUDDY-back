package com.kuddy.common.community.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public PostImage(String fileUrl, Post post) {
        this.fileUrl = fileUrl;
        this.post = post;
    }
}
