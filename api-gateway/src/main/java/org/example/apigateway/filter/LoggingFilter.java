package org.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = getOrCreateTraceId(exchange);

        // Добавим traceId в MDC
        MDC.put(TRACE_ID_HEADER, traceId);

        // Логируем запрос
        ServerHttpRequest request = exchange.getRequest();
        logger.info("Incoming request: {} {}", request.getMethod(), request.getURI());

        // Добавим traceId в заголовки, чтобы он пошёл дальше
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header(TRACE_ID_HEADER, traceId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> MDC.clear()); // Очищаем MDC после запроса
    }

    private String getOrCreateTraceId(ServerWebExchange exchange) {
        List<String> traceIds = exchange.getRequest().getHeaders().get(TRACE_ID_HEADER);
        return (traceIds != null && !traceIds.isEmpty())
                ? traceIds.get(0)
                : UUID.randomUUID().toString();
    }

    @Override
    public int getOrder() {
        return -1; // Высокий приоритет
    }
}