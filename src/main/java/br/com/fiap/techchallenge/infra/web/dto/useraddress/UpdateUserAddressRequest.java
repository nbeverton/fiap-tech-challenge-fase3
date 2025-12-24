package br.com.fiap.techchallenge.infra.web.dto.useraddress;

public record UpdateUserAddressRequest(
        String type,
        String label,
        boolean principal
) {
}
