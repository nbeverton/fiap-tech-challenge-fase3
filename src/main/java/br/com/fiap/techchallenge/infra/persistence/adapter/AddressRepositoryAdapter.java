package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.Address;
import br.com.fiap.techchallenge.core.usecase.out.AddressRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.AddressDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringAddressRepository;
import br.com.fiap.techchallenge.infra.web.mapper.address.AddressMapper;
import org.springframework.stereotype.Component;

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

        AddressDocument document = AddressMapper.toDocument(address);
        AddressDocument saved = repository.save(document);

        return AddressMapper.toDomain(saved);
    }

    @Override
    public void delete(String id) {

        repository.deleteById(id);
    }

    @Override
    public Optional<Address> findById(String id) {
        return repository.findById(id)
                .map(AddressMapper::toDomain);
    }

    @Override
    public List<Address> findAll() {
        return repository.findAll()
                .stream()
                .map(AddressMapper::toDomain)
                .toList();
    }
}
