package br.com.fiap.techchallenge.infra.web.dto.address;

public record AddressResponse(
        String id,
        String postalCode,
        String streetName,
        int streetNumber,
        String additionalInfo,
        String neighborhood,
        String city,
        String stateProvince,
        String country
) {
}
