package com.challenge.ably.domain;

import com.challenge.ably.dto.auth.req.CreatePhoneAuthReqDto;
import com.challenge.ably.util.AuthTypeCode;
import com.challenge.ably.util.TelecomCode;
import com.challenge.ably.util.YnCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "TB_PHONE_AUTH")
@RequiredArgsConstructor
public class PhoneAuth extends CommonBaseDateTime {

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

    @Column(name = "authentication")
    private String authentication;

    @Enumerated(EnumType.STRING)
    @Column(name = "authorized_yn")
    private YnCode authorizedYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthTypeCode authTypeCode;

    @Column(name = "auth_until")
    private LocalDateTime authUntil;

    public void refreshAuthUtil(int minutes) {
        this.authUntil = LocalDateTime.now().plusMinutes(minutes);
    }

    public void setAuthentication(String authentication) {
        this.authorizedYn = YnCode.Y;
        this.authentication = authentication;
    }

    public PhoneAuth(CreatePhoneAuthReqDto reqDto, String authValue, String encPhone) throws Exception {
        this.telecomCode = reqDto.getTelecomCode();
        this.authValue = authValue;
        this.encPhone = encPhone;
        this.authTypeCode = reqDto.getAuthTypeCode();
        this.authorizedYn = YnCode.N;
        this.authUntil = LocalDateTime.now().plusMinutes(3);
    }

}
