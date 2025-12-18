package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;


    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public User execute(String id, User updateUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.updateName(updateUser.getName());
        existingUser.updateEmail(updateUser.getEmail());
        existingUser.updateLogin(updateUser.getLogin());
        existingUser.updatePassword(updateUser.getPassword());

        return userRepository.save(existingUser);
    }
}
