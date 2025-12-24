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

        AddressDocument document = getOrCreate(address.getId());

        document.setPostalCode(address.getPostalCode());
        document.setStreetName(address.getStreetName());
        document.setStreetNumber(address.getStreetNumber());
        document.setAdditionalInfo(address.getAdditionalInfo());
        document.setNeighborhood(address.getNeighborhood());
        document.setCity(address.getCity());
        document.setStateProvince(address.getStateProvince());
        document.setCountry(address.getCountry());

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


    /**
     * Retrieves an existing MongoDB document when updating an entity
     * or creates a new one when performing a create operation.
     *
     * This approach ensures that audit fields such as `createdAt`
     * are preserved during update operations, since MongoDB replaces
     * the entire document on save.
     *
     * @param id the entity identifier
     * @return an existing document or a new instance if the id is null
     */
    private AddressDocument getOrCreate(String id) {
        return id == null
                ? new AddressDocument()
                : repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }
}
