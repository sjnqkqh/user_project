package com.challenge.ably.controller;

import com.challenge.ably.dto.CommonRespDto;
import com.challenge.ably.dto.user.req.CheckLoginIdReqDto;
import com.challenge.ably.dto.user.req.PhoneAuthReqDto;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.service.AuthService;
import com.challenge.ably.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     * @return 휴대전화 인증 정보
     */
    @PostMapping("/api/auth/phone")
    public CommonRespDto createPhoneAuthentication(@RequestBody @Valid PhoneAuthReqDto reqDto) {
        authService.createPhoneAuthentication(reqDto);
        return new CommonRespDto(true);
    }
}
