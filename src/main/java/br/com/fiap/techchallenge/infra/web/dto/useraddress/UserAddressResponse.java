package br.com.fiap.techchallenge.infra.web.dto.useraddress;

public record UserAddressResponse(
        String id,
        String userId,
        String addressId,
        String type,
        String label,
        boolean principal
) {
}
