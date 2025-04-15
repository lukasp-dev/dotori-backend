package com.example.ucamcodeapi.controller;

import com.example.ucamcodeapi.dto.SignupRequestDTO;
import com.example.ucamcodeapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody SignupRequestDTO userDetails) {
        Map<String, String> response = authService.register(
                userDetails.getFirstName(),
                userDetails.getLastName(),
                userDetails.getEmail(),
                userDetails.getPassword(),
                userDetails.getRole()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> userDetails){
        String email = userDetails.get("email");
        String password = userDetails.get("password");

        String token = authService.login(email, password);

        return ResponseEntity.ok(Map.of("token", token));
    }
}
