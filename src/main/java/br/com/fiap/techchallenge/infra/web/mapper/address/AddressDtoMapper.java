package br.com.fiap.techchallenge.infra.web.mapper.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.infra.web.dto.address.AddressResponse;
import br.com.fiap.techchallenge.infra.web.dto.address.CreateAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.address.UpdateAddressRequest;

public class AddressDtoMapper {

    public static Address toDomain(CreateAddressRequest request){

        return new Address(
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

    public static Address toDomain(UpdateAddressRequest request){

        return new Address(
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

    public static AddressResponse toResponse(Address response){

        return new AddressResponse(
                response.getId(),
                response.getPostalCode(),
                response.getStreetName(),
                response.getStreetNumber(),
                response.getAdditionalInfo(),
                response.getNeighborhood(),
                response.getCity(),
                response.getStateProvince(),
                response.getCountry()
        );
    }
}
