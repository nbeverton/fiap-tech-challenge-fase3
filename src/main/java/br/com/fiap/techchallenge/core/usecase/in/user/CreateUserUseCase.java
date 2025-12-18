package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.model.User;

public interface CreateUserUseCase {

    User execute(User user);

}
