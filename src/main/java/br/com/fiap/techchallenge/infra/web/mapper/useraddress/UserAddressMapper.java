package br.com.fiap.techchallenge.infra.web.mapper.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.infra.persistence.documents.UserAddressDocument;

public class UserAddressMapper {

    private UserAddressMapper(){}


    public static UserAddressDocument toDocument(UserAddress domain){

        UserAddressDocument document = new UserAddressDocument();

        document.setId(domain.getId());
        document.setUserId(domain.getUserId());
        document.setAddressId(domain.getAddressId());
        document.setAddressType(domain.getType().name());
        document.setLabel(domain.getLabel());
        document.setPrincipal(domain.isPrincipal());

        return document;
    }


    public static UserAddress toDomain(UserAddressDocument document){

        return new UserAddress(
                document.getId(),
                document.getUserId(),
                document.getAddressId(),
                AddressType.valueOf(document.getAddressType()),
                document.getLabel(),
                document.isPrincipal()
        );
    }

}
