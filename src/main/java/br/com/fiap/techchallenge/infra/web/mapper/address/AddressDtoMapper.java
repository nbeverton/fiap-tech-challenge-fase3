package br.com.fiap.techchallenge.infra.web.mapper.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.infra.web.dto.address.AddressResponse;
import br.com.fiap.techchallenge.infra.web.dto.address.CreateAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.address.UpdateAddressRequest;

public class AddressDtoMapper {

    private AddressDtoMapper(){}

    public static Address toDomain(CreateAddressRequest request, String userId){

        return new Address(
                userId,
                request.postalCode(),
                request.streetName(),
                request.streetNumber(),
                request.additionalInfo(),
                request.neighborhood(),
                request.city(),
                request.stateProvince(),
                request.country()
        );
    }

    public static Address toDomain(UpdateAddressRequest request, String addressId, String userId){

        return new Address(
                addressId,
                userId,
                request.postalCode(),
                request.streetName(),
                request.streetNumber(),
                request.additionalInfo(),
                request.neighborhood(),
                request.city(),
                request.stateProvince(),
                request.country()
        );
    }

    public static AddressResponse toResponse(Address address) {

        return new AddressResponse(
                address.getId(),
                address.getPostalCode(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getAdditionalInfo(),
                address.getNeighborhood(),
                address.getCity(),
                address.getStateProvince(),
                address.getCountry()
        );
    }
}
