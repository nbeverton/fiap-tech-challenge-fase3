package br.com.fiap.techchallenge.infra.web.dto.auth;

public record LoginRequestDTO(
        String login,
        String password
) {}

