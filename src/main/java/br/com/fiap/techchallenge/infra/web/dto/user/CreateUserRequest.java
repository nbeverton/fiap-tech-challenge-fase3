package br.com.fiap.techchallenge.infra.web.dto.user;

public record CreateUserRequest(
        String name,
        String userType,
        String email,
        String login,
        String password,
        String addressId,
        String addressType,
        String label
) {
}
