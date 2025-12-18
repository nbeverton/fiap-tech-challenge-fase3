package br.com.fiap.techchallenge.infra.web.dto.user;

import br.com.fiap.techchallenge.core.domain.enums.UserType;

public record CreateUserRequest (
        String name,
        UserType type,
        String email,
        String login,
        String password
){
    @Override
    public String name() {
        return name;
    }

    @Override
    public UserType type() {
        return type;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String login() {
        return login;
    }

    @Override
    public String password() {
        return password;
    }
}
