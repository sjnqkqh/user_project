package com.challenge.ably.dto.user.resp;

import lombok.Getter;

@Getter
public class LoginRespDto {

    private final String accessToken;


    public LoginRespDto( String accessToken) {
        this.accessToken = accessToken;
    }
}
