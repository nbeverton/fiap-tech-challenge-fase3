package br.com.fiap.techchallenge.core.usecase.impl.address;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.in.address.FindAddressByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;

import java.util.Optional;

public class FindAddressByIdUseCaseImpl implements FindAddressByIdUseCase {

    private final AddressRepositoryPort repository;

    public FindAddressByIdUseCaseImpl(AddressRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Address> execute(String id, String userId) {
        return repository.findByIdAndUserId(id, userId);
    }
}

