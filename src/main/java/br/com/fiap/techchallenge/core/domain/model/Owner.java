package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.UserType;

public class Owner extends User{

    public Owner(String name, String email, String login, String password) {
        super(
                name,
                UserType.OWNER,
                email,
                login,
                password);
    }

    public Owner(String id, String name, String email, String login, String password) {
        super(
                id,
                name,
                UserType.OWNER,
                email,
                login,
                password);
    }
}
