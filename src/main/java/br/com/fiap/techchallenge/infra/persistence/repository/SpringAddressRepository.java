package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.AddressDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringAddressRepository extends MongoRepository<AddressDocument, String> {
}
