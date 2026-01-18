package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.address.DeleteAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;

import java.util.List;

public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressRepositoryPort addressRepository;
    private final UserAddressRepositoryPort userAddressRepository;

    public DeleteAddressUseCaseImpl(AddressRepositoryPort addressRepository,
                                    UserAddressRepositoryPort userAddressRepository) {
        this.addressRepository = addressRepository;
        this.userAddressRepository = userAddressRepository;
    }


    @Override
    public void execute(String addressId) {

        // 1. Garante que o Address existe
        addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));

        // 2. Verifica se há vínculos primários (endereços principais)
        List<UserAddress> primaryLinks =
                userAddressRepository.findPrincipalsByAddressId(addressId);

        if (!primaryLinks.isEmpty()) {
            List<String> userIds = primaryLinks.stream()
                    .map(UserAddress::getUserId)
                    .toList();

            throw CannotDeletePrimaryAddressException.forAddress(id,userIds);
        }

        // 3. Remove TODOS os vínculos remanescentes (secundários)
        List<UserAddress> allLinks =
                userAddressRepository.findByAddressId(addressId);

        for (UserAddress link : allLinks) {
            userAddressRepository.deleteById(link.getId());
        }

        // 4. Por fim, apaga o Address
        addressRepository.delete(addressId);
    }
}
