package com.challenge.ably.service;

import com.challenge.ably.config.CommonException;
import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.dto.auth.req.CheckPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.CreatePhoneAuthReqDto;
import com.challenge.ably.dto.auth.resp.CheckPhoneAuthRespDto;
import com.challenge.ably.repository.PhoneAuthRepository;
import com.challenge.ably.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final PhoneAuthRepository phoneAuthRepository;


    /**
     * 휴대전화 인증 생성
     *
     * @param reqDto 휴대전화 정보
     */
    @Transactional
    public void createPhoneAuthentication(CreatePhoneAuthReqDto reqDto) throws Exception {
        // 입력한 휴대전화 번호 유효성 검사
        if (!RegexUtil.checkPhoneNumberPattern(reqDto.getPhoneNumber())) {
            throw new CommonException("Phone Number validation fail.", ApiExceptionCode.REQUEST_VALIDATION_EXCEPTION);
        }

        // 입력한 휴대 전화 정보로 기존 내역 조회
        Optional<PhoneAuth> phoneAuthOptional = phoneAuthRepository.findFirstByAuthTypeCodeAndEncPhoneAndTelecomCode(
                reqDto.getAuthTypeCode(), EncryptUtil.encryptAES256(reqDto.getPhoneNumber()), reqDto.getTelecomCode()
        );

        // 동일한 휴대전화 정보로 인증을 신청한 내역이 있다면, 유효 시간 갱신
        if (phoneAuthOptional.isPresent()) {
            phoneAuthOptional.get().refreshAuthUtil(3);
            return;
        }

        // 휴대전화 인증 요청 정보 저장
        String authValue = StringUtil.getAuthValue(reqDto.getPhoneNumber());  // 인증 번호는 회원이 입력한 핸드폰 번호 뒷자리 6자리
        String encPhone = EncryptUtil.encryptAES256(reqDto.getPhoneNumber()); // 휴대전화 번호 암호화
        phoneAuthRepository.save(new PhoneAuth(reqDto, authValue, encPhone)); // 인증 정보 생성
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
        Optional<PhoneAuth> phoneAuthOptional = phoneAuthRepository.findFirstByAuthTypeCodeAndEncPhoneAndTelecomCode(
                reqDto.getAuthTypeCode(), EncryptUtil.encryptAES256(reqDto.getPhoneNumber()), reqDto.getTelecomCode()
        );

        if (phoneAuthOptional.isEmpty() // 인증 신청 정보가 없는 경우
                || !StringUtils.equals(phoneAuthOptional.get().getAuthValue(), reqDto.getAuthValue()) // 인증 번호가 다른 경우
                || LocalDateTime.now().isBefore(phoneAuthOptional.get().getAuthUntil()) // 인증 기간이 지난 경우
        ) {
            return CheckPhoneAuthRespDto.builder().isSuccess(false).build();
        }

        // 휴대전화 인증 성공시, UUID 하나를 생성하여 클라이언트에 반환
        return CheckPhoneAuthRespDto.builder().isSuccess(true).authentication(StringUtil.getUUID()).build();
    }

    /**
     * 휴대전화 인증 성공 여부 조회
     */
    public boolean isAuthorized(String phone, TelecomCode telecomCode, AuthTypeCode authTypeCode, String authentication) throws Exception {
        Optional<PhoneAuth> authOptional
                = phoneAuthRepository.findFirstByAuthenticationAndAuthTypeCode(authentication, authTypeCode);

        if (authOptional.isEmpty()) {
            return false;
        }

        PhoneAuth auth = authOptional.get(); // 휴대전화 인증 번호
        return EncryptUtil.match(auth.getEncPhone(), phone) && auth.getTelecomCode() == telecomCode; // 인증여부 성공 반환
    }

}
