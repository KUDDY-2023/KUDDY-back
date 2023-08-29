package com.kuddy.common.community.domain;

import com.kuddy.common.comment.domain.Comment;
import com.kuddy.common.community.exception.ExpiredDateException;
import com.kuddy.common.community.exception.InvalidPostArgumentsException;
import com.kuddy.common.domain.BaseTimeEntity;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.District;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    private String title;

    private String content;

    private Integer people; // only for PostType.JOIN_US

    private LocalDate date; // only for PostType.JOIN_US

    private District district; // only for PostType.JOIN_US

    private PostType postType;

    private String itinerarySpots; // only for PostType.ITINENARY

    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();


    @Builder(builderMethodName = "JoinusBuilder", builderClassName = "JoinusBuilder")
    public Post(String title, String content, Integer people, LocalDate date, District district, PostType postType,
                Member author) {
        validateJoinusPost(date, people, district);
        this.title = title;
        this.content = content;
        this.people = people;
        this.date = date;
        this.district = district;
        this.postType = postType;
        this.author = author;
    }

    @Builder(builderMethodName = "OthersPostBuilder", builderClassName = "OthersPostBuilder")
    public Post(String title, String content, PostType postType, Member author, Subject subject) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.author = author;
        this.subject = subject;
    }

    @Builder(builderMethodName = "ItinearyPostBuilder", builderClassName = "ItinearyPostBuilder")
    public Post(String title, String content, PostType postType, String itinerarySpots, Member author) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.itinerarySpots = itinerarySpots;
        this.author = author;
    }


    //현재 날짜 기준으로 이미 지난 날짜에 대해서는 JoinUs 포스틩 작성 불가
    private void validateJoinusPost(LocalDate date, Integer people, District district) {
        LocalDate currentDate = LocalDate.now();

        if (people == null || district == null || date == null) {
            throw new InvalidPostArgumentsException();
        }

        if (currentDate.isAfter(date)) {
            throw new ExpiredDateException();
        }


    }
}
