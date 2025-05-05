package com.dotori.backend.service;

import com.dotori.backend.model.RefreshToken;
import com.dotori.backend.model.User;
import com.dotori.backend.repository.RefreshTokenRepository;
import com.dotori.backend.repository.UserRepository;
import com.dotori.backend.dto.SignupRequestDTO;
import com.dotori.backend.exception.EmailAlreadyExistsException;
import com.dotori.backend.exception.InvalidCredentialsException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public Map<String, Object> signup(SignupRequestDTO userDetails) {
        if (userRepository.existsByEmail(userDetails.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname(userDetails.getFirstName());
        user.setLastname(userDetails.getLastName());
        user.setName(userDetails.getFirstName() + " " + userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getEmail().endsWith("@dotori.com") ? "ADMIN" : "USER");
        userRepository.save(user);
    
        Map<String, Object> claims = Map.of(
            "userId", user.getId(),
            "email", user.getEmail(),
            "role", user.getRole()
        );
    
        String accessToken = jwtService.create(claims, LocalDateTime.now().plusMinutes(15));
        LocalDateTime refreshExpireAt = LocalDateTime.now().plusDays(7);
        String refreshToken = jwtService.create(Map.of("userId", user.getId()), refreshExpireAt);
    
        refreshTokenRepository.save(RefreshToken.builder()
            .token(refreshToken)
            .expiresAt(refreshExpireAt)
            .user(user)
            .build()
        );
    
        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        );
    }
    

    @Transactional
    public Map<String, Object> login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(InvalidCredentialsException::new);
    
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
    
        refreshTokenRepository.deleteAllByUser(user);
    
        Map<String, Object> claims = Map.of(
            "userId", user.getId(),
            "email", user.getEmail(),
            "role", user.getRole()
        );
    
        String accessToken = jwtService.create(claims, LocalDateTime.now().plusMinutes(15));
        LocalDateTime refreshExpireAt = LocalDateTime.now().plusDays(7);
        String refreshToken = jwtService.create(Map.of("userId", user.getId()), refreshExpireAt);
    
        refreshTokenRepository.save(RefreshToken.builder()
            .token(refreshToken)
            .expiresAt(refreshExpireAt)
            .user(user)
            .build()
        );
    
        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken,
            "user", Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
            )
        );
    }    

    public String refreshAccessToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token has expired");
        }

        User user = storedToken.getUser();

        Map<String, Object> claims = Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "role", user.getRole()
        );

        return jwtService.create(claims, LocalDateTime.now().plusMinutes(15));
    }

    public User findOrCreateUser(String email, String name) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(UUID.randomUUID().toString());
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole("USER"); 
            return userRepository.save(newUser);
        });
    }    
}
