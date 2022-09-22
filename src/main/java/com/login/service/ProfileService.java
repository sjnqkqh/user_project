package com.login.service;

import com.login.config.CommonException;
import com.login.domain.Profile;
import com.login.domain.User;
import com.login.dto.profile.req.UpdateProfileReqDto;
import com.login.dto.user.req.CreateUserReqDto;
import com.login.repository.ProfileRepository;
import com.login.util.FileUploadUtil;
import com.login.util.StringUtil;
import com.login.util.code.ApiExceptionCode;
import com.login.util.code.ImageRootCode;
import com.login.util.code.YnCode;
import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;

    /**
     * 프로필 ID로 프로필 조회
     *
     * @param profileId 프로필 ID
     * @return 프로필 정보
     */
    @Transactional(readOnly = true)
    public Profile searchProfile(Long profileId) {
        return profileRepository.findById(profileId).orElseThrow(() -> {
            throw new CommonException(ApiExceptionCode.BAD_REQUEST_EXCEPTION);
        });
    }

    /**
     * 초기 프로필 정보 생성
     *
     * @param reqDto 회원가입 정보
     * @param user   생성된 유저
     */
    @Transactional
    public void createProfile(CreateUserReqDto reqDto, User user) {
        Profile profile = Profile.builder().user(user).nickname(reqDto.getNickname()).useYn(YnCode.Y).build();
        profileRepository.save(profile);
    }


    @Transactional
    public void updateProfile(User user, UpdateProfileReqDto reqDto, MultipartFile profileImage) {
        // 프로필 정보 조회
        Profile profile = searchProfile(reqDto.getProfileId());

        // 프로필 이미지로 전달된 사진이 없으면 기본 이미지로 대체하여 저장
        String imageName, imageUrl;
        if (profileImage == null || profileImage.isEmpty()) {
            imageName = StringUtil.DEFAULT_PROFILE_IMAGE;
            imageUrl = StringUtil.getImageRootUrl(ImageRootCode.PROFILE, imageName);
            profile.updateProfileImage(imageUrl);
        } else {
            imageName = StringUtils.join(StringUtil.getUUID(), profileImage.getContentType());
            imageUrl = StringUtil.getImageRootUrl(ImageRootCode.PROFILE, imageName);
            FileUploadUtil.save(profileImage, Path.of(imageUrl));
        }
        profile.updateProfileImage(imageUrl);

        // 프로필 이미지 목록에 추가
        List<Profile> profileList = profileRepository.findByUserAndUseYnOrderByProfileIdDesc(user, YnCode.Y);
        Profile lastProfile = profileList.get(0);

        // 직전 프로필 이미지가 기본 이미지라면 해당 이미지를 덮어쓰기
        if (StringUtils.equals(lastProfile.getProfileImgName(), StringUtil.DEFAULT_PROFILE_IMAGE)) {
            lastProfile.updateProfileImage(imageUrl);
        } else {

            //FIXME

        }
    }

}
