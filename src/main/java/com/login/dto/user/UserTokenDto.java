package com.login.dto.user;

import com.login.domain.User;
import com.login.domain.UserToken;
import com.login.domain.redis.RedisUserToken;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenDto {

    private User user;

    private String accessToken;
    private LocalDateTime expiredAt;

    private String refreshToken;
    private LocalDateTime refreshTokenExpiredAt;

    public RedisUserToken toRedisTokenEntity() {
        return new RedisUserToken(accessToken, expiredAt, user.getUserId());
    }

    public UserToken toDatabaseTokenEntity() {
        return new UserToken(user, accessToken, refreshToken, refreshTokenExpiredAt);
    }

}
