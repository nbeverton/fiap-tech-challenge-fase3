package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringAddressRepository;
import org.springframework.stereotype.Component;
import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.infra.persistence.mapper.address.AddressMapper;

import java.util.List;
import java.util.Optional;

@Component
public class AddressRepositoryAdapter implements AddressRepositoryPort {

    private final SpringAddressRepository repository;

    public AddressRepositoryAdapter(SpringAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address save(Address address) {
        return AddressMapper.toDomain(repository.save(AddressMapper.toDocument(address)));
    }

    @Override
    public Optional<Address> findByIdAndUserId(String id, String userId) {
        return repository.findByIdAndUserId(id, userId)
                .map(AddressMapper::toDomain);
    }

    @Override
    public Optional<Address> findById(String id) {
        return repository.findById(id)
                .map(AddressMapper::toDomain);
    }

    @Override
    public List<Address> findAllByUserId(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(AddressMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
