package com.dotori.backend.controller;

import com.dotori.backend.dto.SignupRequestDTO;
import com.dotori.backend.dto.SocialLoginDTO;
import com.dotori.backend.model.User;
import com.dotori.backend.service.AuthService;
import com.dotori.backend.repository.RefreshTokenRepository;
import com.dotori.backend.service.JwtService;
import com.dotori.backend.model.RefreshToken;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import java.time.Duration;
import java.util.Map;
import java.time.LocalDateTime;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @Valid @RequestBody SignupRequestDTO userDetails,
            HttpServletResponse response
    ) {
        Map<String, Object> tokens = authService.signup(userDetails);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", (String) tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(tokens.get("accessToken").toString());
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response 
    ) {
        Map<String, Object> tokens = authService.login(email, password);

        // refreshToken은 HttpOnly 쿠키로 설정
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", (String) tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // accessToken은 JSON으로 응답
        return ResponseEntity.ok((String) tokens.get("accessToken")); 
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(
        @CookieValue("refreshToken") String refreshToken // HttpOnly Cookie
    ) {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);
    
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
    
            response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/social-login")
    public ResponseEntity<String> socialLogin(@RequestBody SocialLoginDTO dto, HttpServletResponse response) {
        User user = authService.findOrCreateUser(dto.getEmail(), dto.getName());

        String accessToken = jwtService.create(
            Map.of("email", user.getEmail(), "role", user.getRole()),
            LocalDateTime.now().plusMinutes(30)  // access token 만료 시간
        );
        String refreshToken = jwtService.create(
            Map.of("email", user.getEmail()),
            LocalDateTime.now().plusDays(7)  // refresh token 만료 시간
        );
        
        refreshTokenRepository.save(RefreshToken.builder()
            .token(refreshToken)
            .expiresAt(LocalDateTime.now().plusDays(7))
            .user(user)
            .build());

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(accessToken);
    }
}
