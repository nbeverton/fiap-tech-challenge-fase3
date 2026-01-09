package br.com.fiap.techchallenge.infra.web.mapper.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.usecase.impl.useraddress.UpdateUserAddressUseCaseImpl;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.UpdateUserAddressUseCase;
import br.com.fiap.techchallenge.infra.web.dto.useraddress.UpdateUserAddressRequest;

public class UpdateUserAddressDtoMapper {

    private UpdateUserAddressDtoMapper(){}

    public static AddressType toAddressType(UpdateUserAddressRequest request){

        return AddressType.valueOf(request.type());
    }

    public static String toLabel(UpdateUserAddressRequest request){

        return request.label();
    }

    public static boolean toPrincipal(UpdateUserAddressRequest request){

        return request.principal();
    }
}
