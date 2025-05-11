package com.dotori.backend.controller;

import com.dotori.backend.dto.SignupRequestDTO;
import com.dotori.backend.dto.SocialLoginDTO;
import com.dotori.backend.dto.LoginDTO;
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
    public ResponseEntity<Map<String, Object>> signup(
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
    
        Map<String, Object> responseBody = Map.of(
            "accessToken", tokens.get("accessToken"),
            "user", tokens.get("user")
        );
    
        return ResponseEntity.ok(responseBody);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
        @RequestBody LoginDTO userDetails,
        HttpServletResponse response
    ) {
        Map<String, Object> tokens = authService.login(userDetails.getEmail(), userDetails.getPassword());
    
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", (String) tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
    
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    
        // accessToke & user info
        Map<String, Object> responseBody = Map.of(
            "accessToken", tokens.get("accessToken"),
            "user", tokens.get("user") 
        );
    
        return ResponseEntity.ok(responseBody);
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
    public ResponseEntity<Map<String, Object>> socialLogin(@RequestBody SocialLoginDTO dto, HttpServletResponse response) {
        User user = authService.findOrCreateUser(dto.getEmail(), dto.getName());
    
        String accessToken = jwtService.create(
            Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole()
            ),
            LocalDateTime.now().plusMinutes(30)
        );
    
        String refreshToken = jwtService.create(
            Map.of("userId", user.getId()),
            LocalDateTime.now().plusDays(7)
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
    
        Map<String, Object> responseBody = Map.of(
            "accessToken", accessToken,
            "user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        );
    
        return ResponseEntity.ok(responseBody);
    }    
}
