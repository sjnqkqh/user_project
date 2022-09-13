package com.login.util;

import ch.qos.logback.core.util.TimeUtil;
import com.login.util.code.TokenTypeCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvideUtil {

    private static final String CLAIM_PERMISSION_PARAMETER_NAME = "permission";
    private static String tokenSecretKey;
    private static String issuer;
    private static String audience;

    @Value("${jwt.token.secret-key}")
    private void setTokenSecretKey(String tokenSecretKey) {
        JwtTokenProvideUtil.tokenSecretKey = tokenSecretKey;
    }

    @Value("${jwt.token.issuer}")
    private void setIssuer(String issuer) {
        JwtTokenProvideUtil.issuer = issuer;
    }

    @Value("${jwt.token.audience}")
    private void setAudience(String audience) {
        JwtTokenProvideUtil.audience = audience;
    }

    private static Key getSigningKey() {
        byte[] keyBytes = tokenSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 생성 공통 로직
     *
     * @param subject       토큰 subject
     * @param expiredAt     토큰 만료 시각
     * @param tokenTypeCode 토큰 유형 코드
     * @return JWT 토큰 문자열
     */
    private static String doGenerateToken(String subject, LocalDateTime expiredAt, TokenTypeCode tokenTypeCode) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put(CLAIM_PERMISSION_PARAMETER_NAME, tokenTypeCode.name());

        return create(claims, expiredAt);
    }

    /**
     * 토큰 생성 공통 로직
     *
     * @param claims    Payload
     * @param expiredAt 만료날짜시간
     * @return 토큰정보 및 만료날짜시간 Map으로 리턴
     */
    private static String create(Claims claims, LocalDateTime expiredAt) {
        return Jwts.builder().setHeaderParam(Header.TYPE, Header.JWT_TYPE).setClaims(claims).setIssuer(issuer).setAudience(audience)
            .setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(TimeUtil.convertLocalDateTimeToDate(expiredAt))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Access Token 생성
     *
     * @param subject subject 정보
     * @return 토큰정보 및 만료날짜시간 Map으로 리턴
     */
    public static String generateAccessToken(String subject) {
        return doGenerateToken(subject, LocalDateTime.now().plusSeconds(TokenTypeCode.ACCESS.getAvailableSeconds()), TokenTypeCode.ACCESS);
    }

    /**
     * Refresh Token 생성
     *
     * @param subject subject 정보
     * @return 토큰정보 및 만료날짜시간 Map으로 리턴
     */
    public static String generateRefreshToken(String subject) {
        return doGenerateToken(subject, LocalDateTime.now().plusSeconds(TokenTypeCode.REFRESH.getAvailableSeconds()), TokenTypeCode.REFRESH);
    }

    /**
     * 토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
     *
     * @param token 토큰 정보
     * @return 토큰에 저장된 Payload 값
     * @throws ExpiredJwtException 토큰만료
     */
    public static Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).requireIssuer(issuer).build().parseClaimsJws(token).getBody();
    }

}
