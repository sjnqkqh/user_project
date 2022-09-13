package com.login.service;

import com.login.config.CommonException;
import com.login.domain.User;
import com.login.dto.user.UserTokenDto;
import com.login.dto.user.req.LoginReqDto;
import com.login.dto.user.req.PasswordResetReqDto;
import com.login.dto.user.req.UserCreateReqDto;
import com.login.dto.user.resp.UserInfoRespDto;
import com.login.repository.UserRepository;
import com.login.util.BcryptUtil;
import com.login.util.EncryptUtil;
import com.login.util.JwtTokenProvideUtil;
import com.login.util.RegexUtil;
import com.login.util.StringUtil;
import com.login.util.code.ApiExceptionCode;
import com.login.util.code.YnCode;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 생성 DTO 유효성 검사
     *
     * @param reqDto 회원 생성 DTO
     */
    @Transactional(readOnly = true)
    public void validateCreateUser(UserCreateReqDto reqDto) {

        // 로그인 ID 유효성 검사
        if (!RegexUtil.checkLoginIdPattern(reqDto.getLoginId())) {
            throw new CommonException("Login ID validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        if (isDuplicateLoginId(reqDto.getLoginId())) {
            throw new CommonException("Login ID duplicate.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 비밀번호 유효성 검사
        if (!RegexUtil.checkPasswordPattern(reqDto.getPassword())) {
            throw new CommonException("Password validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 휴대전화 번호 패턴 유효성 검사
        if (!RegexUtil.checkPhoneNumberPattern(reqDto.getPhone())) {
            throw new CommonException("Phone Number validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }
    }

    /**
     * 로그인 ID 중복 검사
     */
    @Transactional(readOnly = true)
    public boolean isDuplicateLoginId(String loginId) {
        return userRepository.existsByLoginIdAndDeleteYn(loginId, YnCode.Y);
    }

    /**
     * 회원 ID로 회원 정보 조회
     *
     * @param id 유저 ID
     * @return 회원 객체
     */
    @Transactional(readOnly = true)
    public User searchUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new CommonException("User ID does not exist", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION));
    }

    /**
     * 회원 ID로 회원 정보 조회 (UserInfoDto 반환)
     *
     * @param id 유저 ID
     * @return 회원 객체
     */
    @Transactional(readOnly = true)
    public UserInfoRespDto searchUserInfo(Long id) throws Exception {
        return new UserInfoRespDto(searchUser(id));
    }

    /**
     * 로그인 ID와 이메일로 회원정보 조회 (비밀번호 조회 시, 사용)
     *
     * @param loginId 로그인 ID
     * @param email 이메일
     * @return 회원정보
     */
    @Transactional(readOnly = true)
    public User searchUserByLoginIdAndEmail(String loginId, String email) {
        return userRepository.findFirstByLoginIdAndEmailAndDeleteYnOrderByCreatedAtDesc(loginId, email, YnCode.N).orElseThrow(
            ()-> new CommonException(ApiExceptionCode.NOT_EXIST_USER_INFORMATION_ERROR)
        );
    }

    /**
     * 회원 생성
     *
     * @param reqDto 회원 생성 DTO
     */
    @Transactional
    public void createUser(UserCreateReqDto reqDto) throws Exception {
        String encPhone = EncryptUtil.encryptAES256(reqDto.getPhone());
        String hashedPassword = BcryptUtil.bcryptEncode(reqDto.getPassword());
        userRepository.save(new User(reqDto, encPhone, hashedPassword));
    }

    /**
     * 로그인
     *
     * @param reqDto ID, PW
     * @return 로그인 된 회원 ID
     */
    @Transactional(readOnly = true)
    public User login(LoginReqDto reqDto) {
        Optional<User> userOptional = userRepository.findFirstByLoginIdAndDeleteYnOrderByCreatedAtDesc(reqDto.getLoginId(), YnCode.N);
        if (userOptional.isEmpty()) {
            log.info("[UserService.login] Login Fail. ID:" + reqDto.getLoginId());
            throw new CommonException(ApiExceptionCode.LOGIN_FAIL_ERROR);
        }

        User user = userOptional.get();
        if (!BcryptUtil.match(reqDto.getPassword(), user.getEncryptedPassword())) {
            log.info("[UserService.login] Login Fail. Password is Wrong");
            throw new CommonException(ApiExceptionCode.LOGIN_FAIL_ERROR);
        }

        return user;
    }

    /**
     * 로그인 이후 JWT Token 발급
     *
     * @param userId 회원 ID
     * @return Access Token
     */
    @Transactional
    public UserTokenDto giveLoginAuthToken(Long userId) {
        // 회원 정보 조회
        User user = searchUser(userId);

        // JWT 토큰 생성
        String accessToken = JwtTokenProvideUtil.generateAccessToken(StringUtil.getUUID());
        String refreshToken = JwtTokenProvideUtil.generateRefreshToken(StringUtil.getUUID());

        // 각 토큰별 만료시각
        LocalDateTime accessExpiredAt = JwtTokenProvideUtil.extractExpiredAt(accessToken);
        LocalDateTime refreshExpiredAt = JwtTokenProvideUtil.extractExpiredAt(refreshToken);

        return new UserTokenDto(user, accessToken, accessExpiredAt, refreshToken, refreshExpiredAt);
    }

    /**
     * 비밀번호 변경
     *
     * @param reqDto 휴대전화 인증 및 아이디, 이메일, 신규 비밀번호 정보
     */
    @Transactional
    public void resetPassword(PasswordResetReqDto reqDto)  {
        User user = searchUserByLoginIdAndEmail(reqDto.getLoginId(), reqDto.getEmail());
        user.updatePassword(BcryptUtil.bcryptEncode(reqDto.getPassword()));
    }
}
