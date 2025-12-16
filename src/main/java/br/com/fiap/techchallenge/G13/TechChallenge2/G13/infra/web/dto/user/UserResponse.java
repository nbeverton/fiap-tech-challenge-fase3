package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.user;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.enums.UserType;

public record UserResponse(
        String id,
        String name,
        UserType type,
        String email,
        String login
) {
}
