package br.com.fiap.techchallenge.infra.web.mapper.user;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.enums.UserType;
import br.com.fiap.techchallenge.core.usecase.in.user.CreateUserInput;
import br.com.fiap.techchallenge.infra.web.dto.user.CreateUserRequest;

public class CreateUserDtoMapper {

    private CreateUserDtoMapper(){};


    public static CreateUserInput toInput(CreateUserRequest request){

        return new CreateUserInput(
                request.name(),
                UserType.fromString(request.userType()),
                request.email(),
                request.login(),
                request.password(),
                request.addressId(),
                AddressType.fromString(request.addressType()),
                request.label()
        );
    }
}
