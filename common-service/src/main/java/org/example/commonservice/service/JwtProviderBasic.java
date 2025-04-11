package org.example.commonservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.commonservice.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import javax.crypto.SecretKey;
import java.security.Key;

@Slf4j
public abstract class JwtProviderBasic {
    protected final SecretKey jwtAccessSecret;
    protected final SecretKey jwtRefreshSecret;
    protected final long jwtAccessExpiration;
    protected final long jwtRefreshExpiration;


    public JwtProviderBasic(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.expiration.access}") long jwtAccessExpiration,
            @Value("${jwt.expiration.refresh}") long jwtRefreshExpiration
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessExpiration = jwtAccessExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
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
            //log.error("Token expired", expEx);
            throw new AuthenticationException("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            //log.error("Unsupported jwt", unsEx);
            throw new AuthenticationException("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            //log.error("Malformed jwt", mjEx);
            throw new AuthenticationException("Malformed jwt");
        } catch (SignatureException sEx) {
            //log.error("Invalid signature", sEx);
            throw new AuthenticationException("Invalid signature");
        } catch (Exception e) {
            //log.error("invalid token", e);
            throw new AuthenticationException("invalid token");
        }
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

    public String getEmailFromAuthHeader(@NonNull String authHeader) {
        Claims claims = getAccessClaims(getTokenFromAuthHeader(authHeader));
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
