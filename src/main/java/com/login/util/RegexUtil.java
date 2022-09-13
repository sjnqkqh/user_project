package com.login.util;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegexUtil {

    private static final String loginIdPattern = "^[a-zA-Z0-9]{6,12}$";
    private static final String passwordPattern = "^[A-Za-z0-9]{6,12}$";
    private static final String phoneNumberPattern = "^01([0-9])([0-9]{3,4})([0-9]{4})$";

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
