package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.impl.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.user.CreateUserUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.UserRepositoryPort;

public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final UserRepositoryPort userRepository;


    public CreateUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public User execute(User user) {

        return userRepository.save(user);
    }
}
