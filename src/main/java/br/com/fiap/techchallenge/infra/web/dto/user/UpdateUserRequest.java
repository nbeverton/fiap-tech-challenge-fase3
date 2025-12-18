package br.com.fiap.techchallenge.infra.web.dto.user;

public record UpdateUserRequest(
        String name,
        String email,
        String login,
        String password
) {}
