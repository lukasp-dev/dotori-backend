package com.example.ucamcodeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/auth/register", "/user/**").permitAll() // Swagger 및 회원가입 경로는 공개
                        .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .permitAll() // Spring Security의 기본 로그인 페이지 허용
                )
                .logout(logout -> logout.permitAll()); // 로그아웃 허용

        return http.build();
    }
}
