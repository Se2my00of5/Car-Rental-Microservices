package org.example.userservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TraceIdFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TraceIdFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put(TRACE_ID_HEADER, traceId);

        // Логируем запрос
        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURL());

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}