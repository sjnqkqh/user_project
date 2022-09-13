package com.login.config;

import com.login.service.UserTokenService;
import com.login.util.code.ApiExceptionCode;
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

    private final UserTokenService userTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        String accessToken = StringUtils.replaceOnce(request.getHeader(HttpHeaders.AUTHORIZATION), "bearer ", "");
        if (StringUtils.isBlank(accessToken)) {
            throw new CommonException(ApiExceptionCode.HEADER_FIELD_EXCEPTION);
        }

        // 사용 가능한 토큰인지 확인
        Long userId = userTokenService.searchStoredToken(accessToken);

        log.info("[AuthenticationInterceptor.preHandle] Start URI={} accessToken={}", request.getRequestURI(), accessToken);
        request.setAttribute("id", userId); // 회원 ID 정보 Attribute로 전달

        return true;
    }

}
