package com.login.controller;

import com.login.dto.CommonRespDto;
import com.login.dto.profile.req.UpdateProfileReqDto;
import com.login.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PatchMapping("/api/profile")
    public CommonRespDto updateProfile(UpdateProfileReqDto reqDto) {


        return new CommonRespDto(true);
    }


}
