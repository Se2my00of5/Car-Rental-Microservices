package org.example.userservice.service;

import io.jsonwebtoken.*;
import org.example.commonservice.service.JwtProviderBasic;
import org.example.userservice.model.RefreshToken;
import org.example.userservice.model.User;
import org.example.userservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;


@Component
public class JwtProvider extends JwtProviderBasic {
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.expiration.access}") long jwtAccessExpiration,
            @Value("${jwt.expiration.refresh}") long jwtRefreshExpiration,
            RefreshTokenRepository refreshTokenRepository
    ) {
        super(jwtAccessSecret, jwtRefreshSecret, jwtAccessExpiration, jwtRefreshExpiration);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateAccessToken(@NonNull User user) {
        final Instant accessExpirationInstant = Instant.now().plusSeconds(jwtAccessExpiration);
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(accessExpiration)
                .signWith(jwtAccessSecret)
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles())
                .compact();
    }

    public String generateRefreshToken(@NonNull User user) {
        final Instant refreshExpirationInstant = Instant.now().plusSeconds(jwtRefreshExpiration);
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        final String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(refreshExpiration)
                .signWith(jwtRefreshSecret)
                .compact();
        refreshTokenRepository.save(new RefreshToken(token));
        return token;
    }

}
