package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;

public interface UpdateUserUseCase {

    User execute(String id, UpdateUserRequest input);
}
