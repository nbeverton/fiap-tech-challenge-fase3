package br.com.fiap.techchallenge.core.usecase.in.user;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.enums.UserType;

public record CreateUserInput(
        String name,
        UserType userType,
        String email,
        String login,
        String password,
        String addressId,
        AddressType addressType,
        String label
) {
}
