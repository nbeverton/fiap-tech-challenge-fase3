package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAllAddressUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

import java.util.List;

public class FindAllAddressUseCaseImpl implements FindAllAddressUseCase {

    private final AddressRepositoryPort addressRepository;

    public FindAllAddressUseCaseImpl(AddressRepositoryPort addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> execute() {
        return addressRepository.findAll();
    }
}
