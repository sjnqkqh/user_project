package com.challenge.ably.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@Entity(name = "TB_USER_TOKEN")
@NoArgsConstructor
public class UserToken extends CommonBaseDateTime {

    @Id
    @Column(name = "token_id")
    @GeneratedValue
    private Long tokenId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expired_at")
    private LocalDateTime refreshTokenExpiredAt;

    public UserToken(User user, String accessToken, String refreshToken, LocalDateTime refreshTokenExpiredAt) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }
}
