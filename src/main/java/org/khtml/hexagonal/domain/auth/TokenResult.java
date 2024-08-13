package org.khtml.hexagonal.domain.auth;

public record TokenResult(
    String accessToken,
    String refreshToken
) {
}
