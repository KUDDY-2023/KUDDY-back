package com.kuddy.chatserver.chat.dto.response;

import com.kuddy.common.member.domain.Member;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverInfoResDto {
    private String nickname;
    private String profileImageUrl;
    public static ReceiverInfoResDto of(Member receiver){
        return ReceiverInfoResDto.builder()
                .nickname(receiver.getNickname())
                .profileImageUrl(receiver.getProfileImageUrl())
                .build();
    }
}
