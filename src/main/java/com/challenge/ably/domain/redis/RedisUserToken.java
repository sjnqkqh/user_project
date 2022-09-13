package com.challenge.ably.domain.redis;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(timeToLive = 3000L, value = "userToken")
public class RedisUserToken {

    @Id
    private String accessToken;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expiredAt;

    private final Long userId;

    public RedisUserToken(String accessToken,LocalDateTime expiredAt , Long userId) {
        this.accessToken = accessToken;
        this.expiredAt = expiredAt;
        this.userId = userId;
    }
}
