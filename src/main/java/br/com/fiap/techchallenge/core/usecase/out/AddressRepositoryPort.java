package br.com.fiap.techchallenge.core.usecase.out;

import br.com.fiap.techchallenge.core.domain.model.Address;
import java.util.List;
import java.util.Optional;

public interface AddressRepositoryPort {

    Address save(Address address);

    Optional<Address> findById(String id);

    Optional<Address> findByIdAndUserId(String id, String userId);

    List<Address> findAllByUserId(String userId);

    void deleteById(String id);
}


