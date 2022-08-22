package com.challenge.ably.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AutoConfigureMockMvc
public @interface EnableMockMvcUTF8 {
    class Config {

        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            return new CharacterEncodingFilter("UTF-8", true);
        }
    }
}
