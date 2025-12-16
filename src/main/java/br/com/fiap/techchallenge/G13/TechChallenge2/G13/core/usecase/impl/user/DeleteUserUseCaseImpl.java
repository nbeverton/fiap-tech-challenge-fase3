package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.impl.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.user.DeleteUserUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.UserRepositoryPort;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private final UserRepositoryPort userRepository;


    public DeleteUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public void execute(String id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.deleteById(id);
    }
}
