package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringUserRepository extends MongoRepository<UserDocument, String> {
}
