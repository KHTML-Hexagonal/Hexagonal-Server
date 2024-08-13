package org.khtml.hexagonal.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(FrameOptionsConfig::disable))
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(setSessionManagement())
                .authorizeHttpRequests(setAuthorizePath())
                .addFilterBefore()
                .exceptionHandling()

    }

    private Customizer<SessionManagementConfigurer<HttpSecurity>> setSessionManagement() {
        return manage -> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> setAuthorizePath() {
        return authorize -> authorize
                .requestMatchers(
                        new AntPathRequestMatcher("/auth/**"),
                        new AntPathRequestMatcher("/error"),
                        new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/h2-console")
                ).permitAll()
                .anyRequest().authenticated()
    }
}
