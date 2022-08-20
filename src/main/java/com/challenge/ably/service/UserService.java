package com.challenge.ably.service;

import com.challenge.ably.config.CommonException;
import com.challenge.ably.domain.User;
import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.repository.UserRepository;
import com.challenge.ably.util.ApiExceptionCode;
import com.challenge.ably.util.EncryptUtil;
import com.challenge.ably.util.RegexUtil;
import com.challenge.ably.util.YnCode;
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

        if (checkLoginIdDuplicate(reqDto.getLoginId())) {
            throw new CommonException("Login ID duplicate.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 비밀번호 유효성 검사
        if (!RegexUtil.checkPasswordPattern(reqDto.getOriginPassword())) {
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
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginIdAndDeleteYn(loginId, YnCode.Y);
    }


    /**
     * 회원 생성
     *
     * @param reqDto 회원 생성 DTO
     */
    @Transactional
    public void createUser(UserCreateReqDto reqDto) throws Exception {
        String encPhone = EncryptUtil.encryptAES256(reqDto.getPhone());
        String hashedPassword = reqDto.getOriginPassword(); // FIXME 비밀번호 해싱
        userRepository.save(new User(reqDto, encPhone, hashedPassword));
    }

}
