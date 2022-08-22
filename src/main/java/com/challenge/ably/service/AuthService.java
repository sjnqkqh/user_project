package com.challenge.ably.service;

import com.challenge.ably.config.CommonException;
import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.domain.User;
import com.challenge.ably.dto.auth.req.CheckPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.PasswordResetPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.SignInPhoneAuthReqDto;
import com.challenge.ably.dto.auth.resp.CheckPhoneAuthRespDto;
import com.challenge.ably.repository.PhoneAuthRepository;
import com.challenge.ably.util.ApiExceptionCode;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.EncryptUtil;
import com.challenge.ably.util.RegexUtil;
import com.challenge.ably.util.StringUtil;
import com.challenge.ably.util.TelecomCode;
import com.challenge.ably.util.YnCode;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final PhoneAuthRepository phoneAuthRepository;


    /**
     * 회원가입 - 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     */
    @Transactional
    public void signInPhoneAuthentication(SignInPhoneAuthReqDto reqDto) throws Exception {
        // 입력한 휴대전화 번호 유효성 검사
        if (!RegexUtil.checkPhoneNumberPattern(reqDto.getPhoneNumber())) {
            log.info("[AuthService.createPhoneAuthentication] phone:" + reqDto.getPhoneNumber());
            throw new CommonException("Phone Number validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 입력한 휴대 전화 정보로 기존 내역 조회
        Optional<PhoneAuth> phoneAuthOptional = phoneAuthRepository.findFirstByAuthTypeCodeAndEncPhoneAndTelecomCodeOrderByUpdatedAtDesc(
            reqDto.getAuthTypeCode(), EncryptUtil.encryptAES256(reqDto.getPhoneNumber()), reqDto.getTelecomCode()
        );

        // 동일한 휴대전화 정보로 인증을 신청한 내역이 있다면, 유효 시간 갱신
        if (phoneAuthOptional.isPresent()) {
            phoneAuthOptional.get().refreshAuthUtil();
            return;
        }

        // 휴대전화 인증 요청 정보 저장
        String authValue = StringUtil.getAuthValue(reqDto.getPhoneNumber());  // 인증 번호는 회원이 입력한 핸드폰 번호 뒷자리 6자리
        String encPhone = EncryptUtil.encryptAES256(reqDto.getPhoneNumber()); // 휴대전화 번호 암호화
        phoneAuthRepository.save(new PhoneAuth(reqDto, authValue, encPhone)); // 인증 정보 생성
    }

    /**
     * 비밀번호 변경 - 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     */
    @Transactional
    public void passwordResetPhoneAuthentication(PasswordResetPhoneAuthReqDto reqDto, User user) throws Exception {
        // 입력한 휴대전화 번호 유효성 검사
        if (!RegexUtil.checkPhoneNumberPattern(reqDto.getPhoneNumber())) {
            log.info("[AuthService.createPhoneAuthentication] phone number validation fail. phone:" + reqDto.getPhoneNumber());
            throw new CommonException("Phone Number validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 입력한 휴대전화 번호와 기존 회원의 휴대전화 번호가 일치하는지 확인
        if (!EncryptUtil.match(user.getEncryptedPhone(), reqDto.getPhoneNumber())) {
            log.info("[AuthService.createPhoneAuthentication] phone number is not matched. phone:" + reqDto.getPhoneNumber());
            throw new CommonException(ApiExceptionCode.PHONE_NUMBER_IS_NOT_MATCHED_ERROR);
        }

        // 입력한 휴대 전화 정보로 기존 내역 조회
        Optional<PhoneAuth> phoneAuthOptional = phoneAuthRepository.findFirstByAuthTypeCodeAndEncPhoneAndTelecomCodeOrderByUpdatedAtDesc(
            reqDto.getAuthTypeCode(), EncryptUtil.encryptAES256(reqDto.getPhoneNumber()), reqDto.getTelecomCode()
        );

        // 동일한 휴대전화 정보로 인증을 신청한 내역이 있다면, 유효 시간 갱신
        if (phoneAuthOptional.isPresent()) {
            phoneAuthOptional.get().refreshAuthUtil();
            return;
        }

        // 휴대전화 인증 요청 정보 저장
        String authValue = StringUtil.getAuthValue(reqDto.getPhoneNumber());  // 인증 번호는 회원이 입력한 핸드폰 번호 뒷자리 6자리
        String encPhone = EncryptUtil.encryptAES256(reqDto.getPhoneNumber()); // 휴대전화 번호 암호화
        phoneAuthRepository.save(new PhoneAuth(reqDto, user, authValue, encPhone)); // 인증 정보 생성
    }

    /**
     * 휴대전화 인증 확인
     *
     * @param reqDto 휴대전화 인증 데이터
     */
    @Transactional
    public CheckPhoneAuthRespDto phoneAuthentication(CheckPhoneAuthReqDto reqDto) throws Exception {
        // 입력한 휴대전화 번호 유효성 검사
        if (!RegexUtil.checkPhoneNumberPattern(reqDto.getPhoneNumber())) {
            throw new CommonException("Phone Number validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 입력한 휴대 전화 정보로 기존 내역 조회
        Optional<PhoneAuth> phoneAuthOptional = phoneAuthRepository.findFirstByAuthTypeCodeAndEncPhoneAndTelecomCodeOrderByUpdatedAtDesc
            (reqDto.getAuthTypeCode(), EncryptUtil.encryptAES256(reqDto.getPhoneNumber()), reqDto.getTelecomCode());

        // 인증 번호가 다르거나, 인증 가능 기간이 지난 경우
        if (phoneAuthOptional.isEmpty() || !StringUtils.equals(phoneAuthOptional.get().getAuthValue(), reqDto.getAuthValue())
            || LocalDateTime.now().isAfter(phoneAuthOptional.get().getAuthUntil())) {
            return CheckPhoneAuthRespDto.builder().isSuccess(false).build();
        }

        // 이미 UUID가 발급되어 있다면 기존 UUID 반환 (여러번 인증 요청 시)
        PhoneAuth phoneAuth = phoneAuthOptional.get();
        if (StringUtils.isNotBlank(phoneAuth.getAuthentication()) && LocalDateTime.now().isBefore(phoneAuth.getGuaranteeUntil())) {
            return CheckPhoneAuthRespDto.builder().isSuccess(true).authentication(phoneAuth.getAuthentication()).build();
        }

        // 인증 성공시, UUID 하나를 생성하여 클라이언트에 반환
        String authentication = StringUtil.getUUID();
        phoneAuth.setAuthentication(authentication);
        return CheckPhoneAuthRespDto.builder().isSuccess(true).authentication(authentication).build();
    }

    /**
     * 휴대전화 인증 성공 여부 조회
     */
    @Transactional(readOnly = true)
    public Long validatePhoneAuth(String phone, TelecomCode telecomCode, AuthTypeCode authTypeCode,
        String authentication) throws Exception {
        Optional<PhoneAuth> authOptional = phoneAuthRepository.findFirstByAuthenticationAndAuthTypeCode(authentication, authTypeCode);

        // 인증번호가 틀린 경우
        if (authOptional.isEmpty()) {
            log.info("[AuthService.validatePhoneAuth] No authentication information");
            throw new CommonException(ApiExceptionCode.NOT_AUTHENTICATED_PHONE_ERROR);
        }

        // 인증번호에 해당하는 인증 요청은 있으나, 정보가 일치하지 않거나 인증되지 않은 경우
        PhoneAuth auth = authOptional.get();
        if (!EncryptUtil.match(auth.getEncPhone(), phone) || auth.getTelecomCode() != telecomCode || auth.getAuthorizedYn() == YnCode.N) {
            log.info("[AuthService.validatePhoneAuth] Wrong phone information");
            throw new CommonException(ApiExceptionCode.NOT_AUTHENTICATED_PHONE_ERROR);
        }

        // 휴대전화 인증 유효 시간을 초과한 경우
        if (LocalDateTime.now().isAfter(auth.getGuaranteeUntil())) {
            log.info("[AuthService.validatePhoneAuth] Guarantee time is overed. authentication:" + authentication);
            throw new CommonException(ApiExceptionCode.AUTHENTICATION_GUARANTEE_TIME_OVER_ERROR);
        }

        return auth.getPhoneAuthId();
    }

    /**
     * 휴대전화 인증 정보 제거
     */
    @Transactional
    public void deletePhoneAuthHistory(Long authId) {
        phoneAuthRepository.findById(authId).ifPresent(phoneAuthRepository::delete);
    }
}
