package br.com.fiap.techchallenge.core.usecase.in.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;

public interface UpdateUserAddressUseCase {

    UserAddress execute(
            String id,
            AddressType type,
            String label,
            boolean principal
            );

}
