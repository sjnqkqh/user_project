package com.challenge.ably.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

@Slf4j
public class RegexUtil {

    private static final String loginIdPattern = "/^[a-zA-Z]\\w{2,7}$/u";
    private static final String passwordPattern = "/w{6,20}$/u";
    private static final String phoneNumberPattern = "/^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$/u";

    /**
     * 로그인 아이디 패턴 확인
     */
    public static boolean checkLoginIdPattern(String str) {
        return Pattern.matches(loginIdPattern, str);
    }

    /**
     * 비밀번호 패턴 확인
     */
    public static boolean checkPasswordPattern(String str) {
        return Pattern.matches(passwordPattern, str);
    }


    /**
     * 휴대전화 번호 패턴 확인
     */
    public static boolean checkPhoneNumberPattern(String str) {
        return Pattern.matches(phoneNumberPattern, str);
    }
}
