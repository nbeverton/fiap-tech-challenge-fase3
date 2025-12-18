package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.model.User;

import java.util.Optional;

public interface FindUserByIdUseCase {

    Optional<User> execute(String id);
}
