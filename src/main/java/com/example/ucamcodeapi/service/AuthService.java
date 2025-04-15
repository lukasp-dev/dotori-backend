package com.example.ucamcodeapi.service;

import com.example.ucamcodeapi.mysql.model.User;
import com.example.ucamcodeapi.mysql.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user to the system and return a JWT token
     * @param firstName user first name
     * @param lastName user last name
     * @param email    user email
     * @param password user password
     * @param role     user role
     * @return         JWT token string
     */
    public Map<String, String> register(String firstName, String lastName, String email, String password, String role) {
        // check email duplicate
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // hashing pw
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User(firstName, lastName, email, hashedPassword, role);
        userRepository.save(user); // 사용자 저장

        // JWT Claims creation
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("role", user.getRole());

        // JWT token creation
        LocalDateTime expireAt = LocalDateTime.now().plusHours(1);
        String token = jwtService.create(claims, expireAt); // JWT 생성

        // IDa and token return
        return Map.of("id", user.getId().toString(), "token", token);
    }

    /**
     * Authenticate the user and return a JWT token
     * @param email    user email
     * @param password user password
     * @return         JWT token string
     * @throws RuntimeException if the user is not found or the password is invalid
     */
    public String login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("role", user.getRole());

        LocalDateTime expireAt = LocalDateTime.now().plusHours(1);
        return jwtService.create(claims, expireAt);
    }
}
