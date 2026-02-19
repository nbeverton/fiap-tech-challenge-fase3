package br.com.fiap.techchallenge.infra.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.FindUserByLoginPort;
import br.com.fiap.techchallenge.infra.persistence.mapper.user.UserMapper;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringUserRepository;

@Component
public class UserMongoAdapter implements FindUserByLoginPort {

    private final SpringUserRepository repository;

    public UserMongoAdapter(SpringUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository
            .findByLogin(login)
            .map(UserMapper::toDomain);
    }
}

