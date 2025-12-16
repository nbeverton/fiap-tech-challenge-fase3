package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.enums.UserType;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Client;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Owner;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.user.CreateUserRequest;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.user.UpdateUserRequest;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.user.UserResponse;

public class UserDtoMapper {

    public static User toDomain(CreateUserRequest request) {
        if (request.type() == UserType.OWNER) {
            return new Owner(
                    request.name(),
                    request.email(),
                    request.login(),
                    request.password()
            );
        }

        return new Client(
                request.name(),
                request.email(),
                request.login(),
                request.password()
        );
    }

    public static User toDomain(UpdateUserRequest input) {
        return new Client(
                input.name(),
                input.email(),
                input.login(),
                input.password()
        );
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUserType(),
                user.getEmail(),
                user.getLogin()
        );
    }
}
