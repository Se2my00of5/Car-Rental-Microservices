package org.example.apigateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.apigateway.exception.AuthenticationException;
import org.example.apigateway.service.JwtProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtCheckGatewayFilterFactory.Config> {

    private final JwtProvider jwtProvider;

    public JwtCheckGatewayFilterFactory(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();


            Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
            List<String> publicPaths = new ArrayList<>();

            if (route != null) {
                Object raw = route.getMetadata().get("publicPaths");

                if (raw instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) raw;

                    publicPaths = map.entrySet().stream()
                            .sorted(Comparator.comparing(e -> Integer.parseInt(e.getKey().toString())))
                            .map(e -> e.getValue().toString())
                            .collect(Collectors.toList());
                }
            }


            //если путь публичный то пропускаем
            if (publicPaths.contains(request.getPath().value())) {
                return chain.filter(exchange);
            }

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new AuthenticationException("Missing Authorization Header");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AuthenticationException("Invalid Authorization Header");
            }

            String token = authHeader.substring(7);
            log.info("JWT Token: {}", token);
            // Проверяем валидность токена
            jwtProvider.validateAccessToken(token);

            return chain.filter(exchange);
        };
    }
    public static class Config {
        // можно добавить параметры фильтра, если нужно
    }
}