package com.challenge.ably.domain;

import com.challenge.ably.dto.user.req.UserCreateReqDto;
import com.challenge.ably.util.YnCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "delete_yn")
    @Enumerated(EnumType.STRING)
    private YnCode deleteYn;


    public User(UserCreateReqDto reqDto) {
        this.loginId = reqDto.getLoginId();
        this.encryptedPassword = reqDto.getOriginPassword(); // FIXME Password hash
        this.email = reqDto.getEmail(); // FIXME Email Encrypt
        this.nickname = reqDto.getNickname();
        this.encryptedPhone = reqDto.getPhone(); // FIXME Phone Encrypt
        this.deleteYn = YnCode.Y;
    }

}
