package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.UserAddressDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringUserAddressRepository;
import br.com.fiap.techchallenge.infra.persistence.mapper.useraddress.UserAddressMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserAddressRepositoryAdapter implements UserAddressRepositoryPort {

    private final SpringUserAddressRepository repository;

    public UserAddressRepositoryAdapter(SpringUserAddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserAddress save(UserAddress userAddress) {

        UserAddressDocument document = getOrCreate(userAddress.getId());

        document.setUserId(userAddress.getUserId());
        document.setAddressId(userAddress.getAddressId());
        document.setAddressType(userAddress.getType().name());
        document.setLabel(userAddress.getLabel());
        document.setPrincipal(userAddress.isPrincipal());

        UserAddressDocument saved = repository.save(document);
        return UserAddressMapper.toDomain(saved);
    }


    @Override
    public void deleteById(String id) {

        repository.deleteById(id);
    }

    @Override
    public List<UserAddress> findByAddressId(String addressId) {

        return repository.findByAddressId(addressId)
                .stream()
                .map(UserAddressMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserAddress> findPrincipalsByAddressId(String addressId) {
        return repository.findByAddressIdAndPrincipalTrue(addressId)
                .stream()
                .map(UserAddressMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserAddress> findPrincipalsById(String id) {
        return repository.findByIdAndPrincipalTrue(id)
                .stream()
                .map(UserAddressMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserAddress> findByUserId(String userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(UserAddressMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserAddress> findPrincipalByUserId(String userId) {

        return repository.findByUserIdAndPrincipalTrue(userId)
                .map(UserAddressMapper::toDomain);
    }

    @Override
    public Optional<UserAddress> findUserAddressById(String id) {
        return repository.findById(id)
                .map(UserAddressMapper::toDomain);
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
    private UserAddressDocument getOrCreate(String id) {
        return id == null
                ? new UserAddressDocument()
                : repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }
}
