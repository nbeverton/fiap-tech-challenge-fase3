package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.FindUserAddressByUserIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.util.List;

public class FindUserAddressByUserIdUseCaseImpl implements FindUserAddressByUserIdUseCase {

    private final UserAddressRepositoryPort userAddressRepository;

    public FindUserAddressByUserIdUseCaseImpl(UserAddressRepositoryPort userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }


    @Override
    public List<UserAddress> execute(String userId) {

        return userAddressRepository.findByUserId(userId);
    }
}
