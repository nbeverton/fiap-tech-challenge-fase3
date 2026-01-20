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

        //1. Validate if the user exists
        findUserByIdUseCase.execute(userAddress.getUserId());

        //2. Validate if the address exists
        findAddressByIdUseCase.execute(userAddress.getAddressId());

        // 3. Validate if the new link will be the principal one; if so, set the old principal to false
        boolean isPrincipal = userAddress.isPrincipal();

        //4. Search for the current primary address (if it exists)
        if(isPrincipal){
            userAddressRepository.findPrincipalByUserId(userAddress.getUserId())
                    .ifPresent(existingPrincipal -> {
                        existingPrincipal.setPrincipal(false);

                        userAddressRepository.save(existingPrincipal);
                    });
        }

        return userAddressRepository.save(userAddress);
    }
}
