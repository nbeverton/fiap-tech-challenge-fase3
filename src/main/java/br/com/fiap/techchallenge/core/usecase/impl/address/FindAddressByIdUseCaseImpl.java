package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.exception.address.AddressNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

import java.util.Optional;

public class FindAddressByIdUseCaseImpl implements FindAddressByIdUseCase {

    private final AddressRepositoryPort addressRepository;


    public FindAddressByIdUseCaseImpl(AddressRepositoryPort addressRepository){
        this.addressRepository = addressRepository;
    }

    @Override
    public Address execute(String id) {

        return addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));
    }
}
