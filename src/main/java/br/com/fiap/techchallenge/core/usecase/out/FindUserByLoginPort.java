package br.com.fiap.techchallenge.core.usecase.out;

import java.util.Optional;

import br.com.fiap.techchallenge.core.domain.model.User;

public interface FindUserByLoginPort {

    Optional<User> findByLogin(String login);

}

