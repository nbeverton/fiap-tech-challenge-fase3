package br.com.fiap.techchallenge.infra.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.fiap.techchallenge.infra.persistence.documents.AddressDocument;

import java.util.List;
import java.util.Optional;

public interface SpringAddressRepository extends MongoRepository<AddressDocument, String> {
    Optional<AddressDocument> findByIdAndUserId(String id, String userId);
    List<AddressDocument> findAllByUserId(String userId);
}
