package com.kuddy.common.notification.calendar.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoToken {

    private String token_type;
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private Integer refresh_token_expires_in;
    private String scope;
    private String id_token;

}

