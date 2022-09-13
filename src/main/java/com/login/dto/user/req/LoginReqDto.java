package com.login.dto.user.req;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {

    @NotBlank
    String loginId;

    @NotBlank
    String password;
}
