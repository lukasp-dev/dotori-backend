package com.dotori.backend.controller;

import com.dotori.backend.dto.SignupRequestDTO;
import com.dotori.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDTO userDetails) {
        String token = authService.signup(userDetails);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }
} 