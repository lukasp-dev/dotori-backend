package com.example.ucamcodeapi.service;

import com.example.ucamcodeapi.model.User;
import com.example.ucamcodeapi.postgres.repository.UserRepository;
import com.example.ucamcodeapi.dto.SignupRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

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
}
