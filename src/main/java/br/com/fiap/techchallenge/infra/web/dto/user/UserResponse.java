package br.com.fiap.techchallenge.infra.web.dto.user;

import br.com.fiap.techchallenge.core.domain.enums.UserType;

public record UserResponse(
        String id,
        String name,
        UserType type,
        String email,
        String login
) {
}
