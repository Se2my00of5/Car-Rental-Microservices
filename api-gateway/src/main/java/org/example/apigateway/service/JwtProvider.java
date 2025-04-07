package org.example.apigateway.service;


import io.jsonwebtoken.*;
import org.example.apigateway.dto.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import service.JwtProviderBasic;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProvider extends JwtProviderBasic {

    public JwtProvider(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.expiration.access}") long jwtAccessExpiration,
            @Value("${jwt.expiration.refresh}") long jwtRefreshExpiration
    ) {
        super(jwtAccessSecret, jwtRefreshSecret, jwtAccessExpiration, jwtRefreshExpiration);
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
