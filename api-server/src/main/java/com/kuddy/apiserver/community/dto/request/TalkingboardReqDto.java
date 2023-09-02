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
public class TalkingboardReqDto {
    @NotEmpty
    private String title;

    @NotNull
    private String content;
    private Integer people;
    private LocalDate date;
    private String district;
    private List<String> images;
    private String subject;
    @NotEmpty
    private String postType;

    public Post toEntityFromJoinUs(TalkingboardReqDto dto, Member member) {
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

    public Post toEntityFromOthers(TalkingboardReqDto dto, Member member) {
        return Post.OthersPostBuilder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .postType(PostType.OTHERS)
                .subject(Subject.of(dto.getSubject()))
                .author(member)
                .build();
    }
}
