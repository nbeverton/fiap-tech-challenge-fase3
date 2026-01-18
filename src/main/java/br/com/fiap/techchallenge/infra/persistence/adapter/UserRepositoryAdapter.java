package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.UserDocument;
import br.com.fiap.techchallenge.infra.persistence.mapper.user.UserMapper;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringUserRepository repository;


    public UserRepositoryAdapter(SpringUserRepository repository){

        this.repository = repository;
    }


    @Override
    public User save(User user) {

        UserDocument document = getOrCreate(user.getId());

        UserMapper.updateDocument(user, document);

        UserDocument saved = repository.save(document);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(String id) {

        return repository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {

        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login)
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserMapper::toDomain);
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
    private UserDocument getOrCreate(String id) {
        return id == null
                ? new UserDocument()
                : repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found: " + id));
    }
}
