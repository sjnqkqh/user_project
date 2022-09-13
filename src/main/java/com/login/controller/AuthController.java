package com.login.controller;

import com.login.domain.User;
import com.login.dto.CommonRespDto;
import com.login.dto.auth.req.CheckPhoneAuthReqDto;
import com.login.dto.auth.req.PasswordResetPhoneAuthReqDto;
import com.login.dto.auth.req.SignInPhoneAuthReqDto;
import com.login.dto.auth.resp.CheckPhoneAuthRespDto;
import com.login.service.AuthService;
import com.login.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * 회원가입 - 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     * @return 휴대전화 인증 정보
     */
    @PostMapping("/api/auth/phone/sign-in")
    public CommonRespDto signInPhoneAuthentication(@RequestBody @Valid SignInPhoneAuthReqDto reqDto) throws Exception {
        authService.signInPhoneAuthentication(reqDto);
        return new CommonRespDto(true);
    }

    /**
     * 비밀번호 찾기 - 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     * @return 휴대전화 인증 정보
     */
    @PostMapping("/api/auth/phone/password-reset")
    public CommonRespDto passwordResetPhoneAuthentication(@RequestBody @Valid PasswordResetPhoneAuthReqDto reqDto) throws Exception {
        User user = userService.searchUserByLoginIdAndEmail(reqDto.getLoginId(), reqDto.getEmail());
        authService.passwordResetPhoneAuthentication(reqDto, user);
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
