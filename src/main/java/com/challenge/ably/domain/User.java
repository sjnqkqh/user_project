package com.challenge.ably.domain;

import com.challenge.ably.dto.user.req.UserCreateReqDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "enc_password")
    private String encryptedPassword;

    @Column(name = "email")
    private String email;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "enc_phone")
    private String encryptedPhone;

    @Column(name = "refresh_token")
    private String refreshToken;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Builder
    public User(UserCreateReqDto reqDto){
        this.loginId = reqDto.getLoginId();
        this.encryptedPassword = reqDto.getOriginPassword(); // FIXME Password hash
        this.email = reqDto.getEmail(); // FIXME Email Encrypt
        this.nickname = reqDto.getNickname();
        this.encryptedPhone = reqDto.getPhone(); // FIXME Phone Encrypt
    }

}
