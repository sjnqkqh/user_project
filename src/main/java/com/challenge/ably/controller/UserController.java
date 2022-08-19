package com.challenge.ably.controller;

import com.challenge.ably.dto.CommonRespDto;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/user")
    public CommonRespDto createUser(@RequestBody @Valid UserCreateReqDto reqDto) {

        log.info(String.valueOf(userService.createUser(reqDto)));

        return new CommonRespDto(true);
    }

}
