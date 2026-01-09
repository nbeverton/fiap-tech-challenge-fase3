package br.com.fiap.techchallenge.core.usecase.in.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;

public interface UpdateUserAddressUseCase {

    /**
     * Updates a UserAddress.
     *
     * Business rules:
     * - A user must always have exactly one principal address
     * - It is not allowed to remove a principal address without defining another one
     */
    UserAddress execute(
            String id,
            AddressType type,
            String label,
            boolean principal
            );

}
