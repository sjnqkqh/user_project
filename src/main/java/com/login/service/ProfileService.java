package com.login.service;

import com.login.config.CommonException;
import com.login.domain.Profile;
import com.login.domain.ProfileImage;
import com.login.domain.User;
import com.login.dto.profile.req.UpdateProfileReqDto;
import com.login.dto.user.req.CreateUserReqDto;
import com.login.repository.ProfileImageRepository;
import com.login.repository.ProfileRepository;
import com.login.util.FileUploadUtil;
import com.login.util.StringUtil;
import com.login.util.code.ApiExceptionCode;
import com.login.util.code.ImageRootCode;
import com.login.util.code.YnCode;
import java.nio.file.Path;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;

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
     * 신규 프로필 정보 생성 (메인 프로필 X)
     *
     * @param reqDto 회원가입 정보
     * @param user   생성된 유저
     */
    public void createProfile(CreateUserReqDto reqDto, User user){
        createProfile(reqDto, user, YnCode.N);
    }

    /**
     * 프로필 정보 생성
     *
     * @param reqDto 회원가입 정보
     * @param user   생성된 유저
     * @param mainProfileYn 메인 프로필 설정 여부
     */
    @Transactional
    public void createProfile(CreateUserReqDto reqDto, User user, YnCode mainProfileYn) {
        Profile profile = Profile.builder().user(user).nickname(reqDto.getNickname()).mainProfileYn(mainProfileYn).useYn(YnCode.Y).build();
        ProfileImage profileImage = ProfileImage.builder() // 프로필 기본 이미지
            .profile(profile)
            .originFileName(StringUtil.DEFAULT_PROFILE_IMAGE)
            .imgFileName(StringUtil.getImageRootUrl(ImageRootCode.PROFILE, StringUtil.DEFAULT_PROFILE_IMAGE))
            .useYn(YnCode.Y)
            .build();

        profileRepository.save(profile);
        profileImageRepository.save(profileImage);
    }


    /**
     * 프로필 정보 업데이트
     *
     * @param reqDto 프로필 업데이트 정보
     * @param profileImage 새로 등록할 프로필 이미지
     */
    @Transactional
    public void updateProfile(UpdateProfileReqDto reqDto, @Nullable MultipartFile profileImage) {
        // 프로필 정보 업데이트
        Profile profile = searchProfile(reqDto.getProfileId());
        profile.updateProfile(reqDto.getNickname(), reqDto.getIntroduce());

        // 프로필 이미지로 전달된 사진이 없으면 기본 이미지로 대체하여 저장
        String imageName, originFileName, imageUrl;
        if (profileImage == null || profileImage.isEmpty()) {
            imageName = StringUtil.DEFAULT_PROFILE_IMAGE;
            originFileName = StringUtil.DEFAULT_PROFILE_IMAGE;
            imageUrl = StringUtil.getImageRootUrl(ImageRootCode.PROFILE, imageName);
            profile.updateProfileImage(imageUrl);
        } else {
            // 프로필 이미지가 저장된 경우, 이미지 정보 갱신 및 파일 업로드
            imageName = StringUtils.join(StringUtil.getUUID(), profileImage.getContentType());
            originFileName = profileImage.getOriginalFilename();
            imageUrl = StringUtil.getImageRootUrl(ImageRootCode.PROFILE, imageName);
            FileUploadUtil.save(profileImage, Path.of(imageUrl));
        }

        // 프로필 대표 이미지 업데이트
        profile.updateProfileImage(imageUrl);

        // 프로필 이미지 목록에 추가
        Optional<ProfileImage> imageOptional = profileImageRepository.findFirstByProfileAndUseYnOrderByCreatedAtDesc(profile, YnCode.Y);

        // 가장 최근 등록한 프로필 이미지가 기본 이미지라면 해당 이미지를 덮어쓰기
        if (imageOptional.isPresent() && StringUtils.equals(imageOptional.get().getImgFileName(), StringUtil.DEFAULT_PROFILE_IMAGE)) {
            imageOptional.get().updateImageFile(imageUrl, StringUtil.getImageRootUrl(ImageRootCode.PROFILE, imageName));
        } else {
            // 직전 프로필 이미지가 기본 이미지가 아니라면 새 프로필 이미지 추가
            ProfileImage newProfileImage = ProfileImage.builder()
                .profile(profile)
                .originFileName(originFileName)
                .imgFileName(imageName)
                .useYn(YnCode.Y)
                .build();

            profileImageRepository.save(newProfileImage);
        }



    }

}
