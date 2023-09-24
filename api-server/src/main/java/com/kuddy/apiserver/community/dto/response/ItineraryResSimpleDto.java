package com.kuddy.apiserver.community.dto.response;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ItineraryResSimpleDto {
    private Long id;
    private String title;
    private String content;
    private List<Spot> spots;
    private Long authorId;
    private LocalDateTime createdDate;
    private Integer commentNo;

    public static ItineraryResSimpleDto of(Post post, List<Spot> spots, Integer commentNo) {
        return ItineraryResSimpleDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent().length() > 95 ? post.getContent().substring(0, 95) : post.getContent())
                .spots(spots)
                .authorId(post.getAuthor().getId())
                .createdDate(post.getCreatedDate())
                .commentNo(commentNo)
                .build();
    }
}
