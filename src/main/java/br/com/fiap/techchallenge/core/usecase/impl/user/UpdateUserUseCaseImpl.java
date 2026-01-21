package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserInput;
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
        // 1. Ensure that the user exists
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // 2. Validate unique LOGIN (if it has been changed)
        if (input.login() != null && !input.login().equals(existingUser.getLogin())) {
            userRepository.findByLogin(input.login())
                    .filter(other -> !other.getId().equals(id)) // ignora o próprio
                    .ifPresent(other -> {
                        throw new UserAlreadyExistsException(
                                "Login already in use: " + input.login()
                        );
                    });
        }

        // 3. Validate unique EMAIL (if it has been changed)
        if (input.email() != null && !input.email().equals(existingUser.getEmail())) {
            userRepository.findByEmail(input.email())
                    .filter(other -> !other.getId().equals(id))
                    .ifPresent(other -> {
                        throw new UserAlreadyExistsException(
                                "Email already in use: " + input.email()
                        );
                    });
        }

        // 4. Apply changes to the aggregate
        //    (domain layer validates format, null/blank values, etc.)
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

        // 5. Persist changes
        return userRepository.save(existingUser);
    }
}
