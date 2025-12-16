package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.adapter;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.UserRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.documents.UserDocument;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper.user.UserMapper;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository.SpringUserRepository;
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

        UserDocument document = UserMapper.toDocument(user);
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
}
