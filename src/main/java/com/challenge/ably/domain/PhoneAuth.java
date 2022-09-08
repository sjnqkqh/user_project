package com.challenge.ably.domain;

import com.challenge.ably.dto.auth.req.PasswordResetPhoneAuthReqDto;
import com.challenge.ably.dto.auth.req.SignInPhoneAuthReqDto;
import com.challenge.ably.util.code.AuthTypeCode;
import com.challenge.ably.util.code.TelecomCode;
import com.challenge.ably.util.code.YnCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@Table(name = "TB_PHONE_AUTH")
@RequiredArgsConstructor
public class PhoneAuth extends CommonBaseDateTime {

    @Id
    @GeneratedValue
    @Column(name = "auth_id")
    private Long phoneAuthId;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @Column(name = "enc_phone")
    private String encPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "telecom_code")
    private TelecomCode telecomCode;

    @Column(name = "auth_value")
    private String authValue;

    @Column(name = "authentication")
    private String authentication; //휴대폰 인증코드 (회원가입, 비밀번호 변경시 확인)

    @Enumerated(EnumType.STRING)
    @Column(name = "authorized_yn")
    private YnCode authorizedYn;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthTypeCode authTypeCode;

    @Column(name = "auth_until")
    private LocalDateTime authUntil;

    @Column(name = "guarantee_until")
    private LocalDateTime guaranteeUntil;

    /**
     * 휴대폰 인증 정보 초기화
     */
    public void refreshAuthUtil() {
        this.authorizedYn = YnCode.N;
        this.authUntil = LocalDateTime.now().plusMinutes(3);
        this.authentication = null;
        this.guaranteeUntil = null;
    }

    /**
     * 휴대폰 인증 성공
     *
     * @param authentication 휴대폰 인증코드 (회원가입, 비밀번호 변경시 확인)
     */
    public void setAuthentication(String authentication) {
        this.authorizedYn = YnCode.Y;
        this.guaranteeUntil = LocalDateTime.now().plusMinutes(30);
        this.authentication = authentication;
    }

    /**
     * 회원가입 - 휴대폰 인증 생성자
     *
     * @param reqDto    인증 요청 데이터
     * @param authValue 휴대폰 인증 번호
     * @param encPhone  암호화 된 휴대폰 번호
     */
    public PhoneAuth(SignInPhoneAuthReqDto reqDto, String authValue, String encPhone) {
        this.telecomCode = reqDto.getTelecomCode();
        this.authValue = authValue;
        this.encPhone = encPhone;
        this.authTypeCode = AuthTypeCode.SIGN_IN;
        this.authorizedYn = YnCode.N;
        this.authUntil = LocalDateTime.now().plusMinutes(3);
    }

    /**
     * 비밀번호 변경 - 휴대폰 인증 생성자
     *
     * @param reqDto    인증 요청 데이터
     * @param authValue 휴대폰 인증 번호
     * @param encPhone  암호화 된 휴대폰 번호
     */
    public PhoneAuth(PasswordResetPhoneAuthReqDto reqDto, User user,String authValue, String encPhone) {
        this.user = user;
        this.telecomCode = reqDto.getTelecomCode();
        this.authValue = authValue;
        this.encPhone = encPhone;
        this.authTypeCode = AuthTypeCode.PASSWORD_RESET;
        this.authorizedYn = YnCode.N;
        this.authUntil = LocalDateTime.now().plusMinutes(3);
    }

}
