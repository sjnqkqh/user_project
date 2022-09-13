package com.login.service;

import com.login.domain.UserToken;
import com.login.domain.redis.RedisUserToken;
import com.login.dto.user.UserTokenDto;
import com.login.repository.RedisUserTokenRepository;
import com.login.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserTokenService {

    private final RedisUserTokenRepository redisUserTokenRepository;
    private final UserTokenRepository userTokenRepository;

    @Transactional
    public void saveUserToken(UserTokenDto tokenDto){
        RedisUserToken redisUserToken = tokenDto.toRedisTokenEntity();
        UserToken userToken = tokenDto.toDatabaseTokenEntity();

        // Redis에 엑세스 토큰 저장
        redisUserTokenRepository.save(redisUserToken);

        // DB에 access + refresh 토큰 저장
        log.info(userToken.toString());
        userTokenRepository.save(userToken);

        new UserToken();
    }
}
