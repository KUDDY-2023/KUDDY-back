package com.kuddy.apiserver.community.dto.request;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import com.kuddy.common.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItineraryReqDto {
    private String title;
    private String content;
    private List<String> images;
    private String postType;
    private List<Long> spots;

    public Post toEntity(ItineraryReqDto dto, String spots, Member member) {
        return Post.ItinearyPostBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postType(PostType.ITINERARY)
                .itinerarySpots(spots)
                .author(member)
                .build();
    }
}