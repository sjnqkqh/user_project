package com.login.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class TimeUtil {

    /**
     * LocalDateTime -> Date(java.util) 변환 함수
     *
     * @param localDateTime 변환할 LocalDateTime 객체
     * @return 변환된 Date 객체
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * Date -> LocalDateTime (java.util) 변환 함수
     *
     * @param date 변환할 LocalDateTime 객체
     * @return 변환된 Date 객체
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }


}
