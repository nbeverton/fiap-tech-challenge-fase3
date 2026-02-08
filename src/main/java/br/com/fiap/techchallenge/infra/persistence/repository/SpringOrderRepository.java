package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringOrderRepository
        extends MongoRepository<OrderDocument, String> {
}
