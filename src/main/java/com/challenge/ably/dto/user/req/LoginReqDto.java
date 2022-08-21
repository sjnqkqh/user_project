package com.challenge.ably.dto.user.req;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginReqDto {

    @NotBlank
    String loginId;

    @NotBlank
    String password;
}
