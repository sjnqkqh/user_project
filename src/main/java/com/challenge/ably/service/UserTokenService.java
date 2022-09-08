package com.challenge.ably.service;

import com.challenge.ably.domain.UserToken;
import com.challenge.ably.domain.redis.RedisUserToken;
import com.challenge.ably.dto.user.UserTokenDto;
import com.challenge.ably.repository.RedisUserTokenRepository;
import com.challenge.ably.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final RedisUserTokenRepository redisUserTokenRepository;
    private final UserTokenRepository userTokenRepository;

    public void saveUserToken(UserTokenDto tokenDto){
        RedisUserToken redisUserToken = tokenDto.toRedisTokenEntity();
        UserToken userToken = tokenDto.toDatabaseTokenEntity();

        // Redis에 엑세스 토큰 저장
        redisUserTokenRepository.save(redisUserToken);

        // DB에 access + refresh 토큰 저장
        userTokenRepository.save(userToken);

        new UserToken();
    }
}
