package br.com.fiap.techchallenge.core.usecase.in.address;

import br.com.fiap.techchallenge.core.domain.model.Address;

import java.util.Optional;

public interface FindAddressByIdUseCase {

    Optional<Address> execute(String id);

}
