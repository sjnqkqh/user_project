package com.challenge.ably.dto.auth.req;

import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CheckPhoneAuthReqDto {

    @NotNull
    private TelecomCode telecomCode;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private AuthTypeCode authTypeCode;

    @NotBlank
    private String authValue;

}
