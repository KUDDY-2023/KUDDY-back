package com.kuddy.apiserver.community.dto.response;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.spot.domain.Spot;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ItineraryResDto {
    private Long id;
    private String title;
    private String content;
    private List<Spot> spots;
    private Long author_id;
    private LocalDateTime createdDate;

    public static ItineraryResDto of(Post post, List<Spot> spots) {
        return ItineraryResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent().length() > 95 ? post.getContent().substring(0, 95) : post.getContent())
                .spots(spots)
                .author_id(post.getAuthor().getId())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
