package org.example.userservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.model.RefreshToken;
import org.example.userservice.model.User;
import org.example.userservice.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final long jwtAccessExpiration;
    private final long jwtRefreshExpiration;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.expiration.access}") long jwtAccessExpiration,
            @Value("${jwt.expiration.refresh}") long jwtRefreshExpiration,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessExpiration = jwtAccessExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
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

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtAccessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(@NonNull String token) {
        Claims claims = getAccessClaims(token);
        return claims.getSubject();
    }

    public String getTokenFromAuthHeader(@NonNull String authHeader) {
        String token = null;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }

}
