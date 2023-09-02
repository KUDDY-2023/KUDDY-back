package com.kuddy.common.comment.domain;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String content;
    private boolean isRemoved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", updatable = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 부모 댓글이 삭제되어도 자식 댓글은 남아 있음
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    public void remove() {
        this.isRemoved = true;
    }

    public void addChild(Comment child) {
        childList.add(child);
    }

    public void setParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    @Builder
    public Comment(String content, Member writer, Post post) {
        this.content = content;
        this.isRemoved = false;
        this.writer = writer;
        this.post = post;
    }

    public List<Comment> findCommentListToDelete() {
        List<Comment> result = new ArrayList<>();
        if (this.parent != null) {
            processReply(result);
        } else {
            processComment(result);
        }

        return result;
    }

    private void processReply(List<Comment> result) {
        Comment parentComment = this.parent;

        if (parentComment.isRemoved() && parentComment.isAllChildRemoved()) {
            result.addAll(parentComment.getChildList());
            result.add(parentComment);
        }
    }

    private void processComment(List<Comment> result) {
        if (isAllChildRemoved()) {
            result.add(this);
            result.addAll(this.getChildList());
        }
    }


    private boolean isAllChildRemoved() {
        if (childList == null) {
            return true;
        }
        return getChildList().stream()
                .allMatch(comment -> comment.isRemoved);
    }


}
