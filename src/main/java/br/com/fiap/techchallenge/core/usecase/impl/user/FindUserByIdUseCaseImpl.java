package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

import java.util.Optional;

public class FindUserByIdUseCaseImpl implements FindUserByIdUseCase {

    private final UserRepositoryPort userRepository;


    public FindUserByIdUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> execute(String id) {
        return userRepository.findById(id);
    }
}
