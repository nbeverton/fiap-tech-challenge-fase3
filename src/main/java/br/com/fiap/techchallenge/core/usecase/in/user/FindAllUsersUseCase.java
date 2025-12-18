package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.model.User;

import java.util.List;

public interface FindAllUsersUseCase {

    List<User> execute();
}
