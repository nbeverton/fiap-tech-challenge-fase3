package br.com.fiap.techchallenge.infra.security.filter;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secretBase64;

    private SecretKey signingKey() {
        if (secretBase64 == null || secretBase64.isBlank()) {
            throw new IllegalStateException("security.jwt.secret is not configured (Base64 expected).");
        }
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}