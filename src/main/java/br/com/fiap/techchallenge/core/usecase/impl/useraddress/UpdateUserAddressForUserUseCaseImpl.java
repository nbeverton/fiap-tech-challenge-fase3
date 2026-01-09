package br.com.fiap.techchallenge.core.usecase.impl.useraddress;

import br.com.fiap.techchallenge.core.domain.enums.AddressType;
import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.useraddress.CannotRemovePrincipalAddressException;
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

        //1. Fetch the link
        UserAddress existingUserAddress = userAddressRepository.findUserAddressById(userAddressId)
                .orElseThrow(() -> new UserAddressNotFoundException(userAddressId));

        //2.
        if(!existingUserAddress.getUserId().equals(userId)){
            throw new UserAddressNotFoundException(userAddressId);
        }

        boolean isPrincipal = existingUserAddress.isPrincipal();

        //3. Rule: Cannot Remove Primary Address
        if(isPrincipal && !principal){
            throw new CannotRemovePrincipalAddressException(userId);
        }

        //4. If Setting as Primary, Uncheck the Current One
        if (principal && !isPrincipal) {
            userAddressRepository.findPrincipalByUserId(userId)
                    .ifPresent(existingPrincipal -> {
                        existingPrincipal.setPrincipal(false);
                        userAddressRepository.save(existingPrincipal);
                    });
        }

        //5. Update Fields
        existingUserAddress.updateType(addressType);
        existingUserAddress.updateLabel(label);
        existingUserAddress.setPrincipal(principal);

        userAddressRepository.save(existingUserAddress);

        //6.
        List<UserAddress> links = userAddressRepository.findByUserId(userId);

        //7.
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
