package br.com.fiap.techchallenge.core.usecase.in.auth;

public record LoginResult(
        String userId,
        String token
) {
}

