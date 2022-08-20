package com.challenge.ably.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiExceptionCode {

    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "Bad Request Exception"),
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.FORBIDDEN, "E0002", "Token Expired"),
    NO_HANDLER_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "Page not found"),
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "E0003", "Method argument validation fail"),

    PHONE_AUTHORIZE_FAIL_ERROR(HttpStatus.BAD_REQUEST, "E0100", "Fail to phone authorize"),
    NOT_AUTHORIZED_PHONE_ERROR(HttpStatus.BAD_REQUEST, "E0101", "It's not authorized phone information"),

    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E9999", "Unknown error");;


    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;

    ApiExceptionCode(HttpStatus httpStatus, String code, String msg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

}
