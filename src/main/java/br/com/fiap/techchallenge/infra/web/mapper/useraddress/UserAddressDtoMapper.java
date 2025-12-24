package br.com.fiap.techchallenge.infra.web.mapper.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.CreateUserAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UpdateUserAddressRequest;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UserAddressResponse;

public class UserAddressDtoMapper {

    private UserAddressDtoMapper(){}


    public static UserAddress toDomain(CreateUserAddressRequest request){

        return new UserAddress(
                request.userId(),
                request.addressId(),
                AddressType.valueOf(request.type()),
                request.label(),
                request.principal()
        );
    }


    public static void toDomain(
            UserAddress domain,
            UpdateUserAddressRequest request){

        domain.updateType(AddressType.valueOf(request.type()));
        domain.updateLabel(request.label());
        domain.updatePrincipal(request.principal());
    }


    public static UserAddressResponse toResponse(UserAddress domain){

        return new UserAddressResponse(
                domain.getId(),
                domain.getUserId(),
                domain.getAddressId(),
                domain.getType().name(),
                domain.getLabel(),
                domain.isPrincipal()
        );
    }
}
