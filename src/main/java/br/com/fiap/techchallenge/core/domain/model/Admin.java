package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.UserType;

public class Admin extends User{

    public Admin(String name, String email, String login, String password) {
        super(
                name,
                UserType.ADMIN,
                email,
                login,
                password);
    }

    public Admin(String id, String name, String email, String login, String password) {
        super(
                id,
                name,
                UserType.ADMIN,
                email,
                login,
                password);
    }
}
