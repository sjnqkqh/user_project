package com.challenge.ably.controller;

import com.challenge.ably.dto.CommonRespDto;
import com.challenge.ably.dto.user.req.CheckLoginIdReqDto;
import com.challenge.ably.dto.user.req.PhoneAuthReqDto;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.service.UserService;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     *
     * @param reqDto 회원가입 데이터
     * @return 회원가입 완료 여부
     */
    @PostMapping("/api/user")
    public CommonRespDto createUser(@RequestBody @Valid UserCreateReqDto reqDto) {
        // 회원가입 입력 데이터 유효성 검사
        userService.validateCreateUser(reqDto);

        // 회원 생성
        userService.createUser(reqDto);

        return new CommonRespDto(true);
    }

    /**
     * 로그인 ID 중복 검사
     *
     * @param reqDto 로그인 ID
     * @return 로그인 ID 사용 가능 여부
     */
    @GetMapping("/api/user/check-duplicate")
    public CommonRespDto checkLoginIdDuplicate(@RequestParam @Valid CheckLoginIdReqDto reqDto) {
        return new CommonRespDto(userService.checkLoginIdDuplicate(reqDto.getLoginId()));
    }



}
