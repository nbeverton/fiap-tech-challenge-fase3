package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.DeleteUserAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.util.List;

public class DeleteUserAddressUseCaseImpl implements DeleteUserAddressUseCase {

    private final UserAddressRepositoryPort userAddressRepository;
    private final AddressRepositoryPort addressRepository;

    public DeleteUserAddressUseCaseImpl(UserAddressRepositoryPort userAddressRepository, AddressRepositoryPort addressRepository) {
        this.userAddressRepository = userAddressRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void execute(String userAddressId) {

        // 1. Check if exists any link
        UserAddress existing = userAddressRepository.findUserAddressById(userAddressId)
                .orElseThrow(() -> new UserAddressNotFoundException(userAddressId));

        //2. Find links with users
        List<UserAddress> principalLinks =
                userAddressRepository.findPrincipalsById(userAddressId);

        // 3. Rule: cannot delete a primary address
        if (!principalLinks.isEmpty()) {

            List<String> userIds = principalLinks.stream()
                    .map(UserAddress::getUserId)
                    .toList();

            throw CannotDeletePrimaryAddressException.forUserAddress(userAddressId, userIds);
        }

        //4. Delete UserAddress
        userAddressRepository.deleteById(existing.getId());
    }
}
