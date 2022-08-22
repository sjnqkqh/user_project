package com.challenge.ably.dto.auth.req;

import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInPhoneAuthReqDto {

    private TelecomCode telecomCode;

    private String phone;

    private AuthTypeCode authTypeCode;

}
