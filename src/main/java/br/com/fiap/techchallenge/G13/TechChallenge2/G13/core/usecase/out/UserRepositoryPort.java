package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(String id);

    List<User> findAll();

    void deleteById(String id);

}
