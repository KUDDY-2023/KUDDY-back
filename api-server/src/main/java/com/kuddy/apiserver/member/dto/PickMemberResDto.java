package com.kuddy.apiserver.member.dto;

import com.kuddy.common.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PickMemberResDto {
    private Long memberId;
    private String nickname;
    private String profileImageUrl;
    private String introduce;

    public static PickMemberResDto of(Member member){
        return PickMemberResDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .introduce(member.getProfile().getIntroduce())
                .build();
    }
}
