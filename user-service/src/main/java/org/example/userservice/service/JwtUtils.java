package org.example.userservice.service;


import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.domain.JwtAuthentication;
import org.example.userservice.model.Role;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUsername(claims.get("username", String.class));
        jwtInfoToken.setEmail(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        List<LinkedHashMap<String, Object>> roleMaps = claims.get("roles", List.class);

        return roleMaps.stream()
                .map(map -> Role.builder()
                        .id(Long.valueOf(map.get("id").toString()))
                        .name((String) map.get("name"))
                        .build())
                .collect(Collectors.toSet());
    }

}