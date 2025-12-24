package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.FindUserAddressByAddressIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.util.List;

public class FindUserAddressByAddressIdUseCaseImpl implements FindUserAddressByAddressIdUseCase {

    private final UserAddressRepositoryPort userAddressRepository;

    public FindUserAddressByAddressIdUseCaseImpl(UserAddressRepositoryPort userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public List<UserAddress> execute(String addressId) {

        return userAddressRepository.findByAddressId(addressId);
    }
}
