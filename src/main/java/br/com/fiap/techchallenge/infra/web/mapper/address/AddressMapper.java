package br.com.fiap.techchallenge.infra.web.mapper.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.infra.persistence.documents.AddressDocument;

public class AddressMapper {

    private AddressMapper(){}


    public static AddressDocument toDocument(Address address){

        AddressDocument document = new AddressDocument();

        document.setId(address.getId());
        document.setPostalCode(address.getPostalCode());
        document.setStreetName(address.getStreetName());
        document.setStreetNumber(address.getStreetNumber());
        document.setAdditionalInfo(address.getAdditionalInfo());
        document.setNeighborhood(address.getNeighborhood());
        document.setCity(address.getCity());
        document.setStateProvince(address.getStateProvince());
        document.setCountry(address.getCountry());

        return document;
    }


    public static Address toDomain(AddressDocument document){

        return new Address(
                document.getId(),
                document.getPostalCode(),
                document.getStreetName(),
                document.getStreetNumber(),
                document.getAdditionalInfo(),
                document.getNeighborhood(),
                document.getCity(),
                document.getStateProvince(),
                document.getCountry()
        );
    }
}
