package com.challenge.ably.domain;

import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.util.YnCode;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "TB_USER")
@NoArgsConstructor
public class User extends CommonBaseDateTime {

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    private Long userId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "enc_password")
    private String encryptedPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "enc_phone")
    private String encryptedPhone;

    @Column(name = "access_expired_at")
    private LocalDateTime accessExpiredAt;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private YnCode deleteYn;


    public User(UserCreateReqDto reqDto, String encryptedPhone, String hashedPassword) {
        this.loginId = reqDto.getLoginId();
        this.encryptedPassword = hashedPassword;
        this.email = reqDto.getEmail();
        this.nickname = reqDto.getNickname();
        this.encryptedPhone = encryptedPhone;
        this.deleteYn = YnCode.N;
    }

    public void setAccessToken(String accessToken, LocalDateTime expiredAt) {
        this.accessToken = accessToken;
        this.accessExpiredAt = expiredAt;
    }

}
