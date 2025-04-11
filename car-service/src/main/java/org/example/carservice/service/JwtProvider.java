package org.example.carservice.service;

import org.example.commonservice.service.JwtProviderBasic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
