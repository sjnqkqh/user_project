package com.login.util;

import com.login.util.code.ImageRootCode;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    private static final String PROFILE_IMAGE_ROOT = "/images/profile/";
    private static final String POSTING_IMAGE_ROOT = "/images/posting/";

    public static final String DEFAULT_PROFILE_IMAGE = "default_profile_image.jpeg";

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

    public static String getImageRootUrl(ImageRootCode rootCode, String fileName) {
        return StringUtils.join(rootCode.getFolderRoot() + fileName);
    }


}
