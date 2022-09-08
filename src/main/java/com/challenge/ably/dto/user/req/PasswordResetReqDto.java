package com.challenge.ably.dto.user.req;

import com.challenge.ably.util.code.TelecomCode;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetReqDto {

    @NotBlank
    private String loginId;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    @NotNull
    private TelecomCode telecomCode;

    @NotBlank
    private String authentication;

}
