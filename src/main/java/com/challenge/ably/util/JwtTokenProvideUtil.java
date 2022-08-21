package com.challenge.ably.util;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvideUtil {

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

    private static Key getSigningKey(){
        System.out.println("tokenSecretKey = " + tokenSecretKey);
        byte [] keyBytes = tokenSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static String doGenerateToken(String subject, Long expireTime){
        String token = Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setClaims(Jwts.claims().setSubject(subject))
            .setIssuer(issuer)
            .setAudience(audience)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(expireTime))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();


        return token;
    }

}
