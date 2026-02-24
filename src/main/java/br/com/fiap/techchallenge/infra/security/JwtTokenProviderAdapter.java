package br.com.fiap.techchallenge.infra.security;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.fiap.techchallenge.core.usecase.out.security.TokenProviderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    @Value("${security.jwt.secret}")
    private String secretBase64;

    @Override
    public String generateToken(String userId, String userType) {

        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalStateException("security.jwt.secret is not configured (Base64 expected).");
        }

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secretBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("security.jwt.secret must be Base64 encoded.", e);
        }

        var key = Keys.hmacShaKeyFor(keyBytes);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000L * 60 * 60 * 4);

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", userType)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
