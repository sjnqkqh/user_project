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
import com.login.util.code.TokenTypeCode;
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

    /**
     * 회원 로그인 JWT 토큰 저장
     *
     * @param user 회원 객체
     * @return JWT 토큰 정보
     */
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

        // Redis에서 기존 토큰은 제거하고, DB에 토큰 데이터 업데이트

        return tokenDto;
    }

    /**
     * JWT 토큰 갱신
     *
     * @param accessToken 갱신할 Access Token
     * @return 토큰 갱신 결과
     */
    @Transactional
    public UserTokenDto renewAccessUserToken(String accessToken) {
        // DB에도 해당 Access Token이 없을 경우 -> 토큰 재발급 필요함
        UserToken userToken = userTokenRepository.findFirstByAccessToken(accessToken).orElseThrow(
            () -> {
                throw new CommonException(ApiExceptionCode.TOKEN_NOT_EXIST_ERROR);
            }
        );

        // DB에 토큰 데이터가 존재하지만, Refresh Token의 재사용 기간도 만료된 경우 -> 재로그인 필요
        if (userToken.getRefreshTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CommonException(ApiExceptionCode.TOKEN_EXPIRED_EXCEPTION);
        }

        // Refresh Token은 유효기간이 남아있을 경우, 새로운 AccessToken 발급
        User user = userToken.getUser();

        String newAccessToken = JwtTokenProvideUtil.generateAccessToken(StringUtil.getUUID());
        LocalDateTime newAccessExpiredAt = JwtTokenProvideUtil.extractExpiredAt(accessToken);
        userToken.updateAccessToken(newAccessToken);

        // 인가를 마친 Refresh Token의 사용가능 시간이 일정 수준 이하라면 Refresh Token 데이터도 업데이트
        String refreshToken = userToken.getRefreshToken();
        LocalDateTime refreshExpiredAt = userToken.getRefreshTokenExpiredAt();

        // Refresh 토큰 유지 시간이 2시간 이하인 경우
        if (refreshExpiredAt.minusHours(2).isBefore(LocalDateTime.now())) {
            refreshToken = JwtTokenProvideUtil.generateRefreshToken(StringUtil.getUUID());
            refreshExpiredAt = LocalDateTime.now().plusSeconds(TokenTypeCode.REFRESH.getAvailableSeconds());
            userToken.updateRefreshToken(refreshToken, refreshExpiredAt); // Refresh Token 업데이트
        }

        // 새로운 Access 토큰을 Redis에 저장하고, 기존에 Redis에 저장되어 있던 토큰은 파기
        UserTokenDto tokenDto = new UserTokenDto(user, newAccessToken, newAccessExpiredAt, refreshToken, refreshExpiredAt);
        redisUserTokenRepository.save(tokenDto.toRedisTokenEntity());
        redisUserTokenRepository.findById(accessToken).ifPresent(redisUserTokenRepository::delete);

        return tokenDto;
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
            if (redisUserTokenOptional.get().getExpiredAt().isBefore(LocalDateTime.now())) {
                throw new CommonException(ApiExceptionCode.ACCESS_TOKEN_EXPIRED_EXCEPTION);
            }

            return redisUserTokenOptional.get().getUserId();
        }

        // DB에도 토큰 정보가 존재하지 않을 경우
        UserToken userToken = userTokenRepository.findFirstByAccessToken(accessToken).orElseThrow(() -> {
            throw new CommonException(ApiExceptionCode.TOKEN_NOT_EXIST_ERROR);
        });

        // DB에 토큰이 존재하지만, 유효기간이 만료된 경우
        if (userToken.getRefreshTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CommonException(ApiExceptionCode.TOKEN_EXPIRED_EXCEPTION);
        }

        return userToken.getTokenId();
    }


}
