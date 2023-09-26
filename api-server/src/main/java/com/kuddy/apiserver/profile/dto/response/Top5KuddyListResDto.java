package com.kuddy.apiserver.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kuddy.common.member.domain.Member;
import com.kuddy.common.profile.domain.KuddyLevel;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top5KuddyListResDto {
    private List<Top5KuddyResDto> top5KuddyList;
    private long size;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Top5KuddyResDto{
        private Long memberId;
        private Long profileId;
        private String nickname;
        private String profileImageUrl;
        private KuddyLevel kuddyLevel;
        private String recentReview;

        public static Top5KuddyResDto from(Member member, String recentReview){
            return Top5KuddyResDto.builder()
                    .memberId(member.getId())
                    .profileId(member.getProfile().getId())
                    .profileImageUrl(member.getProfileImageUrl())
                    .nickname(member.getNickname())
                    .recentReview(recentReview)
                    .kuddyLevel(member.getProfile().getKuddyLevel())
                    .build();
        }
    }

    public static Top5KuddyListResDto of(List<Top5KuddyResDto> top5KuddyList){
        return Top5KuddyListResDto.builder()
                .top5KuddyList(top5KuddyList)
                .size(top5KuddyList.size())
                .build();
    }


}
