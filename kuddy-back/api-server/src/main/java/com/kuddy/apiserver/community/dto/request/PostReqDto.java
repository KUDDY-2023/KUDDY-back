package com.kuddy.apiserver.community.dto.request;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import com.kuddy.common.community.domain.Subject;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.spot.domain.District;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReqDto {
    @NotEmpty
    private String title;
    @NotNull
    private String content;
    private List<String> images;
    private String postType;
    private List<Long> spots;
    private Integer people;
    private LocalDate date;
    private String district;
    private String subject;


    public Post toEntityFromItinerary(PostReqDto dto, String spots, Member member) {
        return Post.ItinearyPostBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postType(PostType.ITINERARY)
                .itinerarySpots(spots)
                .author(member)
                .build();
    }

    public Post toEntityFromJoinUs(PostReqDto dto, Member member) {
        return Post.JoinusBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .people(dto.getPeople())
                .date(dto.getDate())
                .district(District.of(dto.getDistrict()))
                .postType(PostType.JOIN_US)
                .author(member)
                .build();
    }

    public Post toEntityFromOthers(PostReqDto dto, Member member) {
        return Post.OthersPostBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postType(PostType.OTHERS)
                .subject(Subject.of(dto.getSubject()))
                .author(member)
                .build();
    }
}