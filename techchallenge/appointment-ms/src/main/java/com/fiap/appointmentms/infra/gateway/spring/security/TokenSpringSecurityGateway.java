package com.fiap.appointmentms.infra.gateway.spring.security;

import com.fiap.appointmentms.core.domain.User;
import com.fiap.appointmentms.core.gateway.TokenGateway;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class TokenSpringSecurityGateway implements TokenGateway {

    @Value("${jwt.secret.key:ABC123}")
    private String jwtKey;

    @Value("${jwt.expiration.time:3600}")
    private Long jwtExpiration;

    @Override
    public String generate(User user) {
        return Jwts.builder()
                .subject(user.login())
                .claim("authorities", List.of(user.type().name()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(jwtExpiration, ChronoUnit.SECONDS)))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public boolean validate(String token, User user) {
        try {
            var subject = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return subject.equals(user.login());
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public String extract(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtKey)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
