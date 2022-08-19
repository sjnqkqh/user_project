package com.challenge.ably.domain;

import com.challenge.ably.dto.user.req.UserCreateReqDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "tb_user")
@NoArgsConstructor
public class User extends CommonBaseDateTime{

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    public User(UserCreateReqDto reqDto) {
        this.loginId = reqDto.getLoginId();
        this.encryptedPassword = reqDto.getOriginPassword(); // FIXME Password hash
        this.email = reqDto.getEmail(); // FIXME Email Encrypt
        this.nickname = reqDto.getNickname();
        this.encryptedPhone = reqDto.getPhone(); // FIXME Phone Encrypt
    }

}
