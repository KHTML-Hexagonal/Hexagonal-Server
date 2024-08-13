package org.khtml.hexagonal.domain.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.user.UserRepository;
import org.khtml.hexagonal.global.config.JwtProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final JwtProperty jwtProperty;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    public boolean validateAccessTokenFromRequest(ServletRequest servletRequest, String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtProperty.getSecretKey().getBytes())).build()
                    .parseSignedClaims(token).getPayload();
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            servletRequest.setAttribute("exception", "MalformedJwtException");
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            servletRequest.setAttribute("exception", "ExpiredJwtException");
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            servletRequest.setAttribute("exception", "UnsupportedJwtException");
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            servletRequest.setAttribute("exception", "IllegalArgumentException");
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}

