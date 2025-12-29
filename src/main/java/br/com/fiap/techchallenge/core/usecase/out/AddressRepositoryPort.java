package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepositoryPort {

    Address save(Address address);

    void delete(String id);

    Optional<Address> findById(String id);

    List<Address> findAll();
}
