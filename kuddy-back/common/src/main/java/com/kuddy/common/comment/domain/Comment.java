package com.kuddy.common.comment.domain;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 부모 댓글이 삭제되어도 자식 댓글은 남아 있음
    @OneToMany(mappedBy = "parent")
    private List<Comment> childList = new ArrayList<>();

    public void delete() {
        this.isDeleted = true;
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
        this.isDeleted = false;
        this.writer = writer;
        this.post = post;
    }

    public List<Comment> findRemoovableCommentList() {
        List<Comment> result = new ArrayList<>();
        if (parent != null && parent.isDeleted && parent.isAllChildDeleted()) {// 대댓글인 경우: 부모 댓글  & 부모댓글의 자식들의  모두 isDeleted == true -> db에서 삭제
            result.addAll(parent.getChildList());
            result.add(parent);
        } else if (isAllChildDeleted()) { // 댓글인 경우 : 자식댓글의 isDeleted가 모두 true -> db 삭제
            result.add(this);
            result.addAll(getChildList());
        }

        return result;
    }

    private boolean isAllChildDeleted() {
        return getChildList().stream()
                .map(Comment::isDeleted)
                .allMatch(isDeleted -> isDeleted == true);
    }

}
