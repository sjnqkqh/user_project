package com.challenge.ably.config;

import com.challenge.ably.domain.User;
import com.challenge.ably.service.UserService;
import com.challenge.ably.util.JwtTokenProvideUtil;
import com.challenge.ably.util.code.ApiExceptionCode;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String accessToken = StringUtils.replaceOnce(request.getHeader(HttpHeaders.AUTHORIZATION), "bearer ", "");
        Claims claims = JwtTokenProvideUtil.extractAllClaims(accessToken);
        LocalDateTime accessTokenExpiredAt = LocalDateTime.parse(String.valueOf(claims.get("expiredAt")));
        User user = userService.searchUser(Long.parseLong(String.valueOf(claims.get("userId"))));

        if (StringUtils.isBlank(accessToken)) {
            throw new CommonException(ApiExceptionCode.HEADER_FIELD_EXCEPTION);
        }
        log.info("[AuthenticationInterceptor.preHandle] Start URI={} accessToken={}", request.getRequestURI(), accessToken);

        if (LocalDateTime.now().isAfter(accessTokenExpiredAt)) { // Access Token 만료 여부 확인
            log.error("[AuthenticationInterceptor.preHandle] Expire access token. expiredAt={}", accessTokenExpiredAt);
            throw new CommonException(ApiExceptionCode.TOKEN_EXPIRED_EXCEPTION);
        }

        request.setAttribute("id", user.getUserId()); // 회원 ID 정보 Attribute로 전달

        return true;
    }

}
