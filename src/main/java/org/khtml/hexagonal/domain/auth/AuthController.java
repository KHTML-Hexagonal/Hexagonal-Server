package org.khtml.hexagonal.domain.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.khtml.hexagonal.global.support.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResult> login(
            @Valid @RequestBody LoginRequest loginRequest
    ) {
        System.out.println("loginRequest = " + loginRequest);
        return ApiResponse.success(authService.login(loginRequest));
    }
}
