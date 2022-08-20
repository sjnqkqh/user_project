package com.challenge.ably.controller;

import com.challenge.ably.dto.CommonRespDto;
import com.challenge.ably.dto.auth.req.CheckPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.CreatePhoneAuthReqDto;
import com.challenge.ably.dto.auth.resp.CheckPhoneAuthRespDto;
import com.challenge.ably.service.AuthService;
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
    public CommonRespDto createPhoneAuthentication(@RequestBody @Valid CreatePhoneAuthReqDto reqDto) throws Exception {
        authService.createPhoneAuthentication(reqDto);
        return new CommonRespDto(true);
    }

    /**
     * 휴대전화 인증 확인
     *
     * @param reqDto 휴대전화 정보
     * @return 휴대전화 인증 정보
     */
    @PatchMapping("/api/auth/phone")
    public CheckPhoneAuthRespDto phoneAuthentication(@RequestBody @Valid CheckPhoneAuthReqDto reqDto) throws Exception {
        return authService.phoneAuthentication(reqDto);
    }
}
