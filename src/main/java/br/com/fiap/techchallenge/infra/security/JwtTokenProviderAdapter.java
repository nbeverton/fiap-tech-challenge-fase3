package br.com.fiap.techchallenge.infra.security;

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
    private String secret;

    @Override
    public String generateToken(String userId, String userType) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000 * 60 * 60 * 4);

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", userType)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
