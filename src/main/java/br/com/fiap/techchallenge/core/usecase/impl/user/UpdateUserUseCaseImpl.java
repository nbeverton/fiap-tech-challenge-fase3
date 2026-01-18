package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.exception.user.UserAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.user.UserNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserInput;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User execute(String id, UpdateUserInput input) {

        // 1. Garante que o usuário existe
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // 2. Validar LOGIN único (se foi alterado)
        if (input.login() != null && !input.login().equals(existingUser.getLogin())) {
            userRepository.findByLogin(input.login())
                    .filter(other -> !other.getId().equals(id)) // ignora o próprio
                    .ifPresent(other -> {
                        throw new UserAlreadyExistsException(
                                "Login already in use: " + input.login()
                        );
                    });
        }

        // 3. Validar EMAIL único (se foi alterado)
        if (input.email() != null && !input.email().equals(existingUser.getEmail())) {
            userRepository.findByEmail(input.email())
                    .filter(other -> !other.getId().equals(id))
                    .ifPresent(other -> {
                        throw new UserAlreadyExistsException(
                                "Email already in use: " + input.email()
                        );
                    });
        }

        // 4. Aplicar alterações no agregado (domínio valida formato, null/blank, etc.)
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

        // 5. Persistir
        return userRepository.save(existingUser);
    }
}
