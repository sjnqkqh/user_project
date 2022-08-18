package com.challenge.ably.dto.user.req;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserCreateReqDto {

    @NotBlank
    private String loginId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String originPassword;

    @NotBlank
    @Size(min = 1, max = 40)
    private String nickname;

    @NotBlank
    private String phone;

}
