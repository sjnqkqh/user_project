package com.login.util.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageRootCode {

    PROFILE("/images/profile/"),
    POSTING("/images/posting/");

    private final String folderRoot;
}
