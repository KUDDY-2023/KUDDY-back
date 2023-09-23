package com.kuddy.apiserver.comment.dto.response;

import com.kuddy.common.profile.domain.KuddyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WriterInfoDto {
    private Long writerId;
    private String nickname;
    private String profileImageUrl;
    private KuddyLevel kuddyLevel;
}
