package com.login.service;

import com.login.config.CommonException;
import com.login.domain.User;
import com.login.domain.UserToken;
import com.login.domain.redis.RedisUserToken;
import com.login.dto.user.UserTokenDto;
import com.login.repository.RedisUserTokenRepository;
import com.login.repository.UserTokenRepository;
import com.login.util.JwtTokenProvideUtil;
import com.login.util.StringUtil;
import com.login.util.code.ApiExceptionCode;
import java.time.LocalDateTime;
import java.util.Optional;
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
    public UserTokenDto saveUserToken(User user) {
        // 토큰 생성
        String accessToken = JwtTokenProvideUtil.generateAccessToken(StringUtil.getUUID());
        String refreshToken = JwtTokenProvideUtil.generateRefreshToken(StringUtil.getUUID());

        // 각 토큰별 만료시각
        LocalDateTime accessExpiredAt = JwtTokenProvideUtil.extractExpiredAt(accessToken);
        LocalDateTime refreshExpiredAt = JwtTokenProvideUtil.extractExpiredAt(refreshToken);
        UserTokenDto tokenDto = new UserTokenDto(user, accessToken, accessExpiredAt, refreshToken, refreshExpiredAt);

        RedisUserToken redisUserToken = tokenDto.toRedisTokenEntity(); // Redis에 저장할 토큰
        UserToken userToken = tokenDto.toDatabaseTokenEntity(); // DB에 저장할 토큰

        // Redis에 엑세스 토큰 저장
        redisUserTokenRepository.save(redisUserToken);

        // DB에 access + refresh 토큰 저장
        userTokenRepository.save(userToken);

        return tokenDto;
    }

    @Transactional
    public UserTokenDto renewAccessUserToken(String accessToken) {
        // DB에도 해당 Access Token이 없을 경우 -> 토큰 재발급 필요함
        UserToken userToken = userTokenRepository.findFirstByAccessToken(accessToken).orElseThrow(
            () -> {
                throw new CommonException(ApiExceptionCode.TOKEN_NOT_EXIST_ERROR);
            }
        );

        // DB에 토큰 데이터가 존재하지만, Refresh Token의 재사용 기간도 만료된 경우 -> 재로그인 필요
        if (userToken.getRefreshTokenExpiredAt().isAfter(LocalDateTime.now())) {
            throw new CommonException(ApiExceptionCode.TOKEN_EXPIRED_EXCEPTION);
        }

        // Refresh Token은 유효기간이 남아있을 경우, 새로운 AccessToken 발급
        User user = userToken.getUser();
        return saveUserToken(user);
    }

    /**
     * Redis에 AccessToken이 적재되어 있는지 확인
     *
     * @param accessToken 액세스 토큰
     * @return Redis에 토큰 포함 여부
     */
    @Transactional(readOnly = true)
    public Long searchStoredToken(String accessToken) {
        // Redis에 해당 Access Token이 존재할 경우 즉시 회원 PK 반환
        Optional<RedisUserToken> redisUserTokenOptional = redisUserTokenRepository.findById(accessToken);
        if (redisUserTokenOptional.isPresent()) {

            // 데이터가 존재하더라도, 유효 기간을 넘겼을 경우
            if (redisUserTokenOptional.get().getExpiredAt().isBefore(LocalDateTime.now())){
                throw new CommonException(ApiExceptionCode.ACCESS_TOKEN_EXPIRED_EXCEPTION);
            }

            return redisUserTokenOptional.get().getUserId();
        }

        // DB에도 토큰 정보가 존재하지 않을 경우
        UserToken userToken = userTokenRepository.findFirstByAccessToken(accessToken).orElseThrow(() -> {
            throw new CommonException(ApiExceptionCode.TOKEN_NOT_EXIST_ERROR);
        });

        // DB에 토큰이 존재하지만, 유효기간이 만료된 경우
        if (userToken.getRefreshTokenExpiredAt().isBefore(LocalDateTime.now())){
            throw new CommonException(ApiExceptionCode.TOKEN_EXPIRED_EXCEPTION);
        }

        return userToken.getTokenId();
    }


}
