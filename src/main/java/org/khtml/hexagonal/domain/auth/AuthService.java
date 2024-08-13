package org.khtml.hexagonal.domain.auth;

import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.domain.user.User;
import org.khtml.hexagonal.domain.user.UserRepository;
import org.khtml.hexagonal.domain.user.UserType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Transactional
    public TokenResult login(LoginRequest loginRequest) {
        User newUser = User.builder()
                .providerId(loginRequest.providerId())
                .locationConsent(loginRequest.locationConsent())
                .numberOfPersons(loginRequest.numberOfPersons())
                .phoneNumber(loginRequest.phoneNumber())
                .username(loginRequest.username())
                .userType(UserType.valueOf(loginRequest.userType()))
                .build();

        User savedUser = userRepository.save(newUser);
        String accessToken = jwtGenerator.generateAccessToken(savedUser.getId());
        String refreshToken = jwtGenerator.generateRefreshToken();

        return new TokenResult(accessToken, refreshToken);
    }

}
