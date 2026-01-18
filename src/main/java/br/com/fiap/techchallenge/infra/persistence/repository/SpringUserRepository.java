package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringUserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByLogin(String login);

    Optional<UserDocument> findByEmail(String email);

}
