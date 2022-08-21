package com.challenge.ably.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiExceptionCode {

    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "Bad Request Exception"),
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.FORBIDDEN, "E0002", "Token Expired"),
    NO_HANDLER_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "Page not found"),
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "E0004", "Method argument validation fail"),
    HEADER_FIELD_EXCEPTION(HttpStatus.BAD_REQUEST, "E0005", "Header setting error"),
    TOKEN_NOT_EXIST_ERROR(HttpStatus.BAD_REQUEST, "E0006", "Access Token does not exist."),


    PHONE_AUTHENTICATE_FAIL_ERROR(HttpStatus.BAD_REQUEST, "E0100", "Fail to phone authenticate"),
    NOT_AUTHENTICATED_PHONE_ERROR(HttpStatus.BAD_REQUEST, "E0101", "It's not authenticated phone information"),
    AUTHENTICATION_GUARANTEE_TIME_OVER_ERROR(HttpStatus.BAD_REQUEST, "E0102", "Guarantee time is overed"),

    LOGIN_FAIL_ERROR(HttpStatus.BAD_REQUEST, "E0200", "Invalid user login information"),

    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "E9999", "Unknown error");


    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;

    ApiExceptionCode(HttpStatus httpStatus, String code, String msg) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = msg;
    }

}
