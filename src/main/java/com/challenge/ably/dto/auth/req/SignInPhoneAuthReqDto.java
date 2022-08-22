package com.challenge.ably.dto.auth.req;

import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import lombok.Getter;

@Getter
public class SignInPhoneAuthReqDto {

    private TelecomCode telecomCode;

    private String phoneNumber;

    private AuthTypeCode authTypeCode;

}
