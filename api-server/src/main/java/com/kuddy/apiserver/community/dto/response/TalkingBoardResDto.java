package com.kuddy.apiserver.community.dto.response;

import com.kuddy.common.community.domain.Post;
import com.kuddy.common.community.domain.PostType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TalkingBoardResDto {
    private Long id;
    private String postType;
    private String title;
    private String content;
    private Integer people;
    private LocalDate date;
    private String district;
    private List<String> fileUrls;
    private Long author_id;
    private String subject;
    private LocalDateTime createdDate;

    public static TalkingBoardResDto of(Post post, List<String> imgUrlList) {
        if (post.getPostType().equals(PostType.JOIN_US)) {
            return TalkingBoardResDto.builder()
                    .id(post.getId())
                    .postType(post.getPostType().getType())
                    .title(post.getTitle())
                    .content(post.getContent().length() > 95 ? post.getContent().substring(0, 95) : post.getContent())
                    .people(post.getPeople())
                    .date(post.getDate())
                    .district(post.getDistrict().getArea())
                    .fileUrls(imgUrlList)
                    .author_id(post.getAuthor().getId())
                    .createdDate(post.getCreatedDate())
                    .build();
        }
        return TalkingBoardResDto.builder()
                .id(post.getId())
                .postType(post.getPostType().getType())
                .title(post.getTitle())
                .content(post.getContent().length() > 95 ? post.getContent().substring(0, 95) : post.getContent())
                .fileUrls(imgUrlList)
                .author_id(post.getAuthor().getId())
                .subject(post.getSubject().getType())
                .createdDate(post.getCreatedDate())
                .build();

    }

}
