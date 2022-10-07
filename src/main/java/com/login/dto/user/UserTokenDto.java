package com.login.dto.user;

import com.login.domain.User;
import com.login.domain.UserToken;
import com.login.domain.redis.RedisUserToken;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserTokenDto {

    private User user;

    private String accessToken;
    private LocalDateTime expiredAt;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiredAt;

    @Builder
    public UserTokenDto(User user, String accessToken, LocalDateTime expiredAt, String refreshToken, LocalDateTime refreshTokenExpiredAt) {
        this.user = user;
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
    }

    public RedisUserToken toRedisTokenEntity() {
        return new RedisUserToken(accessToken, expiredAt, user.getUserId());
    }

    public UserToken toDatabaseTokenEntity() {
        return new UserToken(user, accessToken, refreshToken, refreshTokenExpiredAt);
    }
}
