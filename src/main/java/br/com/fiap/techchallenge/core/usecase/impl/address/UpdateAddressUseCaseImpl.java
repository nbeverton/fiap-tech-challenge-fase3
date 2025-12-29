package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.UpdateAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

public class UpdateAddressUseCaseImpl implements UpdateAddressUseCase {

    private final AddressRepositoryPort addressRepository;


    public UpdateAddressUseCaseImpl(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }


    @Override
    public Address execute(String id, Address address) {

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));

        existingAddress.updatePostalCode(address.getPostalCode());
        existingAddress.updateStreetName(address.getStreetName());
        existingAddress.updateStreetNumber(address.getStreetNumber());
        existingAddress.updateAdditionalInfo(address.getAdditionalInfo());
        existingAddress.updateNeighborhood(address.getNeighborhood());
        existingAddress.updateCity(address.getCity());
        existingAddress.updateStateProvince(address.getStateProvince());
        existingAddress.updateCountry(address.getCountry());

        return addressRepository.save(existingAddress);
    }
}
