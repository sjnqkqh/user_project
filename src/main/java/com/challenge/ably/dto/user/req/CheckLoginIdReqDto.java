package com.challenge.ably.dto.user.req;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CheckLoginIdReqDto {

    @NotBlank
    String loginId;
}
