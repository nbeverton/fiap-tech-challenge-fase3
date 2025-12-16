package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;

import java.util.Optional;

public interface FindUserByIdUseCase {

    Optional<User> execute(String id);
}
