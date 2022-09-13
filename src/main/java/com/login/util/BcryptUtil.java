package com.login.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptUtil {


    @Resource(name = "passwordEncoder")
    private PasswordEncoder passwordEncoder;

    private static PasswordEncoder encoder;

    @PostConstruct
    private void initialize(){encoder = passwordEncoder;}

    public static String bcryptEncode(String origin) {
        return encoder.encode(origin);
    }

    public static boolean match(String origin, String hash) {
        return encoder.matches(origin, hash);
    }

}
