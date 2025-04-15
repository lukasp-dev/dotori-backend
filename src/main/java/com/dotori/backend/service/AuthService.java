package com.dotori.backend.service;

import com.dotori.backend.model.User;
import com.dotori.backend.repository.UserRepository;
import com.dotori.backend.dto.SignupRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequestDTO userDetails) {
        if (userRepository.existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(userDetails.getRole());

        userRepository.save(user);

        return jwtService.create(
                Map.of(
                        "email", user.getEmail(),
                        "role", user.getRole()
                ),
                LocalDateTime.now().plusDays(1)
        );
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.create(
                Map.of(
                        "email", user.getEmail(),
                        "role", user.getRole()
                ),
                LocalDateTime.now().plusDays(1)
        );
    }
} 