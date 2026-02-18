package br.com.fiap.techchallenge.core.usecase.in.auth;

public record LoginCommand(
        String login,
        String password
) {
}
