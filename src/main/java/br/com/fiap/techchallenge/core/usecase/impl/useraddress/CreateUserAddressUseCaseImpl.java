package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.user.FindUserByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.CreateUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

public class CreateUserAddressUseCaseImpl implements CreateUserAddressUseCase {

    private final UserAddressRepositoryPort userAddressRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindAddressByIdUseCase findAddressByIdUseCase;

    public CreateUserAddressUseCaseImpl(UserAddressRepositoryPort userAddressRepository, FindUserByIdUseCase findUserByIdUseCase, FindAddressByIdUseCase findAddressByIdUseCase) {
        this.userAddressRepository = userAddressRepository;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findAddressByIdUseCase = findAddressByIdUseCase;
    }


    @Override
    public UserAddress execute(UserAddress userAddress) {

        findUserByIdUseCase.execute(userAddress.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userAddress.getUserId())
                );

        findAddressByIdUseCase.execute(userAddress.getAddressId())
                .orElseThrow(() ->
                        new RuntimeException("Address not found: " + userAddress.getAddressId())
                );

        return userAddressRepository.save(userAddress);
    }
}
