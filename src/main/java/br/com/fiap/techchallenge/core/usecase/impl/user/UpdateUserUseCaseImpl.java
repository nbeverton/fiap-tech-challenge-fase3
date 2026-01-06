package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User execute(String id, UpdateUserRequest input) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (input.name() != null) {
            existingUser.updateName(input.name());
        }
        if (input.email() != null) {
            existingUser.updateEmail(input.email());
        }
        if (input.login() != null) {
            existingUser.updateLogin(input.login());
        }
        if (input.password() != null) {
            existingUser.updatePassword(input.password());
        }

        return userRepository.save(existingUser);
    }

}
