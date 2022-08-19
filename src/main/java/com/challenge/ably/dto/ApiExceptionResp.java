package com.challenge.ably.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ApiExceptionResp {

    private final String errCd;
    private final String errMsg;

    @Builder
    public ApiExceptionResp(String errCd, String errMsg) {
        this.errCd = errCd;
        this.errMsg = errMsg;
    }
}