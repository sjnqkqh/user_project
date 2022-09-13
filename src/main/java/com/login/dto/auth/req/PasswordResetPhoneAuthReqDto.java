package com.login.dto.auth.req;

import com.login.util.code.AuthTypeCode;
import com.login.util.code.TelecomCode;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordResetPhoneAuthReqDto {

    private final AuthTypeCode authTypeCode = AuthTypeCode.PASSWORD_RESET;

    @NotBlank
    private  String loginId;

    @Email
    private String email;

    @NotNull
    private TelecomCode telecomCode;

    @NotBlank
    private String phone;


    public PasswordResetPhoneAuthReqDto(String loginId, String email, TelecomCode telecomCode, String phone) {
        this.loginId = loginId;
        this.email = email;
        this.telecomCode = telecomCode;
        this.phone = phone;
    }
}
