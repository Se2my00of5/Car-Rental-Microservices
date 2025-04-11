package org.example.apigateway.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.service.JwtProvider;
import org.example.commonservice.dto.Role;
import org.example.commonservice.exception.ForbiddenException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component

public class AdminRoleCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<AdminRoleCheckGatewayFilterFactory.Config> {

    private final JwtProvider jwtProvider;

    public AdminRoleCheckGatewayFilterFactory(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = authHeader.substring(7);

            Claims claims = jwtProvider.getAccessClaims(token);

            Set<Role> roles = jwtProvider.getRoles(claims);

            boolean isAdmin = roles
                    .stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
            if (!isAdmin) {
                throw new ForbiddenException("Нет доступа");
            }
            // Прокинуть информацию дальше (опционально)
//            ServerHttpRequest request = exchange.getRequest().mutate()
//                    .header("X-User-Roles", String.join(",", roles))
//                    .build();

            return chain.filter(exchange);
        };
    }

    public static class Config {
        // можно добавить параметры фильтра, если нужно
    }
}