package org.khtml.hexagonal.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class JwtProperty {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token-expire-time}")
    private Long accessTokenExpireTime;
    @Value("${jwt.refresh-token-expire-time}")
    private Long refreshTokenExpireTime;

}
