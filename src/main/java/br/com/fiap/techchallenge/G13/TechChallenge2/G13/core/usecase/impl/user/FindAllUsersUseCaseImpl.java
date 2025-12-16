package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.impl.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.user.FindAllUsersUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.UserRepositoryPort;

import java.util.List;

public class FindAllUsersUseCaseImpl implements FindAllUsersUseCase {

    private final UserRepositoryPort userRepository;


    public FindAllUsersUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}
