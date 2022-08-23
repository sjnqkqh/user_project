package com.challenge.ably.dto.auth.req;

import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
