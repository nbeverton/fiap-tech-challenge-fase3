package br.com.fiap.techchallenge.infra.persistence.repository;

import br.com.fiap.techchallenge.infra.persistence.documents.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringPaymentRepository extends MongoRepository<PaymentDocument, String> {

    List<PaymentDocument> findByOrderId(String orderId);

    void saveAll(List<PaymentDocument> payments);
}
