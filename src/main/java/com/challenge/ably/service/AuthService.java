package com.challenge.ably.service;

import com.challenge.ably.domain.PhoneAuth;
import com.challenge.ably.dto.CommonRespDto;
import com.challenge.ably.dto.user.req.PhoneAuthReqDto;
import com.challenge.ably.repository.PhoneAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

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
    public void createPhoneAuthentication(PhoneAuthReqDto reqDto){
        phoneAuthRepository.save(new PhoneAuth(reqDto));

        // 동일한 휴대전화 정보로 인증을 신청한 내역이 있다면, 유효 시간 갱신

        // 인증 번호는 회원이 입력한 핸드폰 번호 뒷자리 6자리

        // 휴대전화 인증 요청 정보 저장

    }

}
