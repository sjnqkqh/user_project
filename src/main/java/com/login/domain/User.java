package com.login.domain;

import com.login.dto.user.req.CreateUserReqDto;
import com.login.util.code.YnCode;
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

    @Column(name = "enc_phone")
    private String encryptedPhone;

    @Column(name = "use_yn")
    @Enumerated(EnumType.STRING)
    private YnCode useYn;


    public User(CreateUserReqDto reqDto, String encryptedPhone, String hashedPassword) {
        this.loginId = reqDto.getLoginId();
        this.encryptedPassword = hashedPassword;
        this.email = reqDto.getEmail();
        this.encryptedPhone = encryptedPhone;
        this.useYn = YnCode.Y;
    }

    public void updatePassword(String encryptedPassword){
        this.encryptedPassword = encryptedPassword;
    }

}
