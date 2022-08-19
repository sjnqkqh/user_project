package com.challenge.ably.config;

import com.challenge.ably.util.ApiExceptionCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{

    private final ApiExceptionCode errorCode;

    public CommonException(ApiExceptionCode errorCode){
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public CommonException(String message, ApiExceptionCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
