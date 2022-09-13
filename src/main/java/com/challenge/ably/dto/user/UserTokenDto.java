package com.challenge.ably.dto.user;

import com.challenge.ably.domain.User;
import com.challenge.ably.domain.UserToken;
import com.challenge.ably.domain.redis.RedisUserToken;
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
