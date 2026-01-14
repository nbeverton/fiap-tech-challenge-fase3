package br.com.fiap.techchallenge.core.usecase.in.user;

public record UpdateUserInput(
        String name,
        String email,
        String login,
        String password){
}
