package com.challenge.ably.domain.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(timeToLive = 3000L, value = "userToken")
public class RedisUserToken {

    @Id
    private String accessToken;

    private final Long userId;

    public RedisUserToken(String accessToken, Long userId) {
        this.accessToken = accessToken;
        this.userId = userId;
    }
}
