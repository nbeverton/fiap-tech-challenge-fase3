package br.com.fiap.techchallenge.infra.web.dto.auth;

public record LoginResponseDTO(
        String userId,
        String token
) {}
