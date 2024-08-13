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
        User user;
        if (!userRepository.existsByProviderId(loginRequest.providerId())) {
            user = User.builder()
                    .providerId(loginRequest.providerId())
                    .locationConsent(loginRequest.locationConsent())
                    .numberOfPersons(loginRequest.numberOfPersons())
                    .phoneNumber(loginRequest.phoneNumber())
                    .username(loginRequest.username())
                    .userType(UserType.valueOf(loginRequest.userType()))
                    .build();
        } else {
            user = userRepository.findByProviderId(loginRequest.providerId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }
        userRepository.save(user);
        String accessToken = jwtGenerator.generateAccessToken(user.getId());
        String refreshToken = jwtGenerator.generateRefreshToken();

        return new TokenResult(accessToken, refreshToken);
    }

}
