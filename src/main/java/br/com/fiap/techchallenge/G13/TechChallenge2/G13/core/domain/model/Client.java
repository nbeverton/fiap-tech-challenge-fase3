package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.enums.UserType;

public class Client extends User{

    public Client(String name, String email, String login, String password) {
        super(
                name,
                UserType.CLIENT,
                email,
                login,
                password);
    }

    public Client(String id, String name, String email, String login, String password) {
        super(
                id,
                name,
                UserType.CLIENT,
                email,
                login,
                password);
    }
}
