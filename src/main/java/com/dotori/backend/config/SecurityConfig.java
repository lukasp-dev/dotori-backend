package com.dotori.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dotori.backend.filter.JwtAuthenticationFilter;
import com.dotori.backend.service.JwtService;
import com.dotori.backend.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SecurityConfig(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/", "/error").permitAll()
            .requestMatchers("/auth/**", "/api/auth/**").permitAll()
            .requestMatchers("/api/schools").permitAll()
            .requestMatchers("/api/essays/**").permitAll()
            .requestMatchers( "/v3/api-docs/**", "/swagger-ui/**","/swagger-ui.html").permitAll()
            .requestMatchers("/api/upload/resume/**").permitAll()
            .requestMatchers("/api/users/**").authenticated()
            .requestMatchers("/api/uploadPersonalInfo").authenticated()
            .requestMatchers("/api/upload/**").authenticated()
            .anyRequest().denyAll() // block others
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
} 