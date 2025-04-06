package org.example.apigateway.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.dto.Role;
import org.example.apigateway.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {
    private final SecretKey jwtAccessSecret;

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
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

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Set<Role> getRoles(Claims claims) {
        List<LinkedHashMap<String, Object>> roleMaps = claims.get("roles", List.class);

        return roleMaps.stream()
                .map(map -> Role.builder()
                        .id(Long.valueOf(map.get("id").toString()))
                        .name((String) map.get("name"))
                        .build())
                .collect(Collectors.toSet());
    }

}
