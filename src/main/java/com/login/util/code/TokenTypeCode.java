package com.login.util.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenTypeCode {

    ACCESS(60L * 15L), // Access Token의 유지 기간은 15분
    REFRESH(60 * 60 * 24L); // Refresh Token의 유지 기간은 24 시간

    private final Long availableSeconds;
}
