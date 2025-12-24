package br.com.fiap.techchallenge.core.usecase.in.useraddress;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;

import java.util.List;

public interface FindUserAddressByAddressIdUseCase {

    List<UserAddress> execute(String addressId);

}
