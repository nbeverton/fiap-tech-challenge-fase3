package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.user;

public record UpdateUserRequest(
        String name,
        String email,
        String login,
        String password
) {}
