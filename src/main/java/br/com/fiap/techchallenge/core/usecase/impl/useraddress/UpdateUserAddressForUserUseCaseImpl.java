package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotDeletePrimaryAddressException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.UserAddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.in.useraddress.UpdateUserAddressForUserUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressSummaryOutput;

import java.util.Comparator;
import java.util.List;

public class UpdateUserAddressForUserUseCaseImpl implements UpdateUserAddressForUserUseCase {

    private final UserAddressRepositoryPort userAddressRepository;
    private final AddressRepositoryPort addressRepository;

    public UpdateUserAddressForUserUseCaseImpl(UserAddressRepositoryPort userAddressRepository, AddressRepositoryPort addressRepository) {
        this.userAddressRepository = userAddressRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<UserAddressSummaryOutput> execute(
            String userId,
            String userAddressId,
            AddressType addressType,
            String label,
            boolean principal
    ) {

        // 1. Fetch the user-address link by its identifier
        UserAddress existingUserAddress = userAddressRepository.findUserAddressById(userAddressId)
                .orElseThrow(() -> new UserAddressNotFoundException(userAddressId));

        // 2. Validate ownership: ensure the address belongs to the given user
        if(!existingUserAddress.getUserId().equals(userId)){
            throw new UserAddressNotFoundException(userAddressId);
        }

        boolean isPrincipal = existingUserAddress.isPrincipal();

        // 3. Business rule: a primary address cannot be unset without assigning another one
        if(isPrincipal && !principal){
            throw CannotDeletePrimaryAddressException.forUser(userId);
        }

        // 4. If the address is being set as primary, unset the current primary address (if any)
        if (principal && !isPrincipal) {
            userAddressRepository.findPrincipalByUserId(userId)
                    .ifPresent(existingPrincipal -> {
                        existingPrincipal.setPrincipal(false);
                        userAddressRepository.save(existingPrincipal);
                    });
        }

        // 5. Update address link attributes
        existingUserAddress.updateType(addressType);
        existingUserAddress.updateLabel(label);
        existingUserAddress.setPrincipal(principal);

        userAddressRepository.save(existingUserAddress);

        // 6. Retrieve all address links associated with the user
        List<UserAddress> links = userAddressRepository.findByUserId(userId);

        // 7. Build summary output, enriching with address data and ordering by primary status
        return links.stream()
                .map(link -> {
                    Address address = addressRepository.findById(link.getAddressId())
                            .orElseThrow(() -> new AddressNotFoundException(link.getAddressId()));

                    return new UserAddressSummaryOutput(
                            link.getType(),
                            address.getStreetName(),
                            address.getStreetNumber(),
                            address.getNeighborhood(),
                            address.getCity(),
                            address.getStateProvince(),
                            address.getAdditionalInfo(),
                            link.isPrincipal()
                    );
                })
                .sorted(Comparator.comparing(UserAddressSummaryOutput::principal).reversed())
                .toList();
    }
}
