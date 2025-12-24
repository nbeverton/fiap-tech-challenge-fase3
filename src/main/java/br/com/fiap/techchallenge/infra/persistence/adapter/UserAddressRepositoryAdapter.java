package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.UserAddress;
import br.com.fiap.techchallenge.core.usecase.out.UserAddressRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.UserAddressDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringUserAddressRepository;
import br.com.fiap.techchallenge.infra.web.mapper.useraddress.UserAddressMapper;
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

        UserAddressDocument document = UserAddressMapper.toDocument(userAddress);
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
}
