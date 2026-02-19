package br.com.fiap.techchallenge.infra.config;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.com.fiap.techchallenge.core.domain.model.User;
import br.com.fiap.techchallenge.core.usecase.out.FindUserByLoginPort;
import br.com.fiap.techchallenge.infra.persistence.mapper.user.UserMapper;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringUserRepository;

@Component
@Primary
public class UserMongoConfig implements FindUserByLoginPort {

    private final SpringUserRepository repository;

    public UserMongoConfig(SpringUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository
                .findByLogin(login)
                .map(UserMapper::toDomain);
    }
}



