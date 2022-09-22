package com.login.controller;

import com.login.dto.CommonRespDto;
import com.login.dto.profile.req.UpdateProfileReqDto;
import com.login.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PatchMapping("/api/profile")
    public CommonRespDto updateProfile(
        @RequestBody UpdateProfileReqDto reqDto,
        @RequestPart @Nullable MultipartFile profileImage) {
        profileService.updateProfile(reqDto,profileImage);

        return new CommonRespDto(true);
    }


}
