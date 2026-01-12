package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
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

        //1. Fetch the link
        UserAddress existingUserAddress = userAddressRepository.findUserAddressById(id)
                        .orElseThrow(() -> new UserAddressNotFoundException(id));

        boolean isPrincipal = existingUserAddress.isPrincipal();

        //2. Rule: Cannot Remove Primary Address
        if(isPrincipal && !principal){
            throw new CannotDeletePrimaryAddressException(
                    existingUserAddress.getUserId()
            );
        }

        //3. If Setting as Primary, Uncheck the Current One
        if(principal && !isPrincipal){
            userAddressRepository.findPrincipalByUserId(existingUserAddress.getUserId())
                    .ifPresent(existingPrincipal -> {
                        existingPrincipal.setPrincipal(false);
                        userAddressRepository.save(existingPrincipal);
                    });
        }

        //4. Update Fields: Modify the relevant data fields of the record as required.
        existingUserAddress.updateType(type);
        existingUserAddress.updateLabel(label);

        //5. Save
        return userAddressRepository.save(existingUserAddress);
    }
}
