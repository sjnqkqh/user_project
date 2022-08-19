package com.challenge.ably.dto.user.req;

import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import lombok.Getter;

@Getter
public class PhoneAuthReqDto {

    private TelecomCode telecomCode;

    private String phoneNumber;

    private AuthTypeCode authTypeCode;

}
