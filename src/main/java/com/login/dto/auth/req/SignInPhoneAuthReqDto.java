package com.login.dto.auth.req;

import com.login.util.code.AuthTypeCode;
import com.login.util.code.TelecomCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInPhoneAuthReqDto {

    private final AuthTypeCode authTypeCode = AuthTypeCode.SIGN_IN;

    @NotNull
    private TelecomCode telecomCode;

    @NotBlank
    private String phone;


    public SignInPhoneAuthReqDto(TelecomCode telecomCode, String phone) {
        this.telecomCode = telecomCode;
        this.phone = phone;
    }
}
