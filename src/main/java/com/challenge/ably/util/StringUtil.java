package com.challenge.ably.util;

import java.util.UUID;

public class StringUtil {

    /**
     * 인증번호 생성 함수 (입력한 휴대전화 번호의 뒷자리 6자리)
     *
     * @param str 휴대전화 번호
     * @return 인증번호 6자리
     */
    public static String getAuthValue(String str) {
        return str.substring(str.length() - 6);
    }

    /**
     * 랜덤 UUID 생성 함수
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
