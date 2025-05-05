package com.dotori.backend.repository;

import com.dotori.backend.model.RefreshToken;
import com.dotori.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteAllByUser(User user);
}
