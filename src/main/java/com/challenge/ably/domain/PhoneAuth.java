package com.challenge.ably.domain;

import com.challenge.ably.dto.user.req.PhoneAuthReqDto;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_PHONE_AUTH")
@RequiredArgsConstructor
public class PhoneAuth extends CommonBaseDateTime{

    @Id
    @GeneratedValue
    @Column(name = "auth_id")
    private Long phoneAuthId;

    @Column(name = "enc_phone")
    private String encPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "telecom_code")
    private TelecomCode telecomCode;

    @Column(name = "auth_value")
    private String authValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthTypeCode authTypeCode;

    @Column(name = "auth_until")
    private LocalDateTime authUntil;

    public PhoneAuth(PhoneAuthReqDto reqDto){
        this.telecomCode = reqDto.getTelecomCode();
        this.authValue = reqDto.getPhoneNumber(); // 휴대전화 인증 번호 생성 로직 필요
        this.encPhone = reqDto.getPhoneNumber(); // FIXME 휴대전화 번호 암호화 필요
        this.authTypeCode = reqDto.getAuthTypeCode();
        this.authUntil = LocalDateTime.now().plusMinutes(3);
    }

}
