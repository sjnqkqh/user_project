package com.login.util.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiExceptionCode {

    BAD_REQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001", "Bad Request Exception"),
    TOKEN_EXPIRED_EXCEPTION(HttpStatus.I_AM_A_TEAPOT, "E0002", "Token Expired"),
    NO_HANDLER_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "E0003", "Page not found"),
    REQUEST_VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "E0004", "Method argument validation fail"),
    HEADER_FIELD_EXCEPTION(HttpStatus.BAD_REQUEST, "E0005", "Header setting error"),
    TOKEN_NOT_EXIST_ERROR(HttpStatus.BAD_REQUEST, "E0006", "Access Token does not exist."),
    ACCESS_TOKEN_EXPIRED_EXCEPTION(HttpStatus.FORBIDDEN, "E0007", "Refresh Token Expired"),

    PHONE_AUTHENTICATE_FAIL_ERROR(HttpStatus.BAD_REQUEST, "E0100", "Fail to phone authenticate"),
    NOT_AUTHENTICATED_PHONE_ERROR(HttpStatus.BAD_REQUEST, "E0101", "It's not authenticated phone information"),
    AUTHENTICATION_GUARANTEE_TIME_OVER_ERROR(HttpStatus.BAD_REQUEST, "E0102", "Guarantee time is overed"),
    NOT_EXIST_USER_INFORMATION_ERROR(HttpStatus.BAD_REQUEST, "E0103", "No user information"),
    PHONE_NUMBER_IS_NOT_MATCHED_ERROR(HttpStatus.BAD_REQUEST, "E0104", "The member's phone number and the phone number you entered do not match"),

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
