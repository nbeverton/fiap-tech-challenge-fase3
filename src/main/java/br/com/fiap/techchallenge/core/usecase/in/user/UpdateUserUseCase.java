package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.model.User;

public interface UpdateUserUseCase {

    User execute(String id, User updateUser);
}
