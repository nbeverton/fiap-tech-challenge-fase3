package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringUserRepository extends MongoRepository<UserDocument, String> {
}
