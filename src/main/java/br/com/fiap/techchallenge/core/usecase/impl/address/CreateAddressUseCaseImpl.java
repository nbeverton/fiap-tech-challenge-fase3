package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.CreateAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

public class CreateAddressUseCaseImpl implements CreateAddressUseCase {

    private final AddressRepositoryPort addressRepository;


    public CreateAddressUseCaseImpl(AddressRepositoryPort addressRepositoryPort){
        this.addressRepository = addressRepositoryPort;
    }

    @Override
    public Address execute(Address address) {
        return addressRepository.save(address);
    }
}
