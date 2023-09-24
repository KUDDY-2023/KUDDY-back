package com.kuddy.apiserver.community.dto.response;

import com.kuddy.apiserver.comment.dto.response.WriterInfoDto;
import com.kuddy.common.community.domain.Post;
import com.kuddy.common.member.domain.Member;
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
    private Long authorId;
    private LocalDateTime createdDate;
    private WriterInfoDto writerInfoDto;

    public static ItineraryResDto of(Post post, List<Spot> spots) {
        Member member = post.getAuthor();
        WriterInfoDto writerInfoDto = new WriterInfoDto(member.getId(), member.getNickname(), member.getProfileImageUrl(), member.getProfile().getKuddyLevel());
        return ItineraryResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .spots(spots)
                .authorId(post.getAuthor().getId())
                .createdDate(post.getCreatedDate())
                .writerInfoDto(writerInfoDto)
                .build();
    }
}
