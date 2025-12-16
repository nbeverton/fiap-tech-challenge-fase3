package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;

import java.util.List;

public interface FindAllUsersUseCase {

    List<User> execute();
}
