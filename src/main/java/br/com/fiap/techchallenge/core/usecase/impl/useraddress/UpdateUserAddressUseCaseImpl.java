package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.UpdateUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

public class UpdateUserAddressUseCaseImpl implements UpdateUserAddressUseCase {

    private final UserAddressRepositoryPort userAddressRepository;

    public UpdateUserAddressUseCaseImpl(UserAddressRepositoryPort userAddressRepository) {
        this.userAddressRepository = userAddressRepository;
    }

    @Override
    public UserAddress execute(String id,
                               AddressType type,
                               String label,
                               boolean principal
    ) {

        UserAddress existingUserAddress = userAddressRepository.findUserAddressById(id)
                        .orElseThrow(() -> new RuntimeException("User Address not found with id: " + id));

        existingUserAddress.updateType(type);
        existingUserAddress.updateLabel(label);
        existingUserAddress.updatePrincipal(principal);

        return userAddressRepository.save(existingUserAddress);
    }
}
