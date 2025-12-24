package br.com.fiap.techchallenge.core.usecase.impl.user;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.in.user.UpdateUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;
import br.com.fiap.techchallenge.infra.web.mapper.user.UserDtoMapper;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;


    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public User execute(String id, UpdateUserRequest input) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        UserDtoMapper.updateDomain(existingUser, input);

        return userRepository.save(existingUser);
    }

}
