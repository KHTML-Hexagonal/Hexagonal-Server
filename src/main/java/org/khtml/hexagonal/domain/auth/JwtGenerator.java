package org.khtml.hexagonal.domain.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.global.config.JwtProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtGenerator {

    private final JwtProperty jwtProperty;

    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .expiration(new Date(System.currentTimeMillis() + jwtProperty.getAccessTokenExpireTime()))
                .signWith(Keys.hmacShaKeyFor(jwtProperty.getSecretKey().getBytes()), Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + jwtProperty.getAccessTokenExpireTime()))
                .signWith(Keys.hmacShaKeyFor(jwtProperty.getSecretKey().getBytes()), Jwts.SIG.HS512)
                .compact();
    }

}
