package org.example.userservice.repository;

import org.example.userservice.model.RefreshToken;
import org.example.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByRefreshToken(String token);
}
