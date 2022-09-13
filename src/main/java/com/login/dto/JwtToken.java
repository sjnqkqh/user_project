package com.login.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtToken {

    private final String token;
    private final LocalDateTime expiredAt;

}
