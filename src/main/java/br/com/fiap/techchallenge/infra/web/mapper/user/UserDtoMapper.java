package br.com.fiap.techchallenge.infra.web.mapper.user;

import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.domain.model.Client;
import br.com.fiap.techchallenge.core.domain.model.Owner;
import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.infra.web.dto.user.CreateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UpdateUserRequest;
import br.com.fiap.techchallenge.infra.web.dto.user.UserResponse;

public class UserDtoMapper {

    private UserDtoMapper(){}

    public static User toDomain(CreateUserRequest request) {

        UserType type = UserType.valueOf(request.type());

        return switch (type){
            case OWNER -> new Owner(
                    request.name(),
                    request.email(),
                    request.login(),
                    request.password()
            );
            case CLIENT -> new Client(
                    request.name(),
                    request.email(),
                    request.login(),
                    request.password()
            );
        };
    }

    public static void updateDomain(User user, UpdateUserRequest input) {

        user.updateName(input.name());
        user.updateEmail(input.email());
        user.updateLogin(input.login());
        user.updatePassword(input.password());
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUserType().name(),
                user.getEmail(),
                user.getLogin()
        );
    }
}
