package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.PaymentDocument;
import br.com.fiap.techchallenge.infra.persistence.mapper.payment.PaymentPersistenceMapper;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringPaymentRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final SpringPaymentRepository repository;

    public PaymentRepositoryAdapter(SpringPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {

        PaymentDocument document = PaymentPersistenceMapper.toDocument(payment);
        repository.save(document);
        return PaymentPersistenceMapper.toDomain(document);
    }

    @Override
    public Optional<Payment> findById(String paymentId) {

        return repository.findById(paymentId)
                .map(PaymentPersistenceMapper::toDomain);
    }

    @Override
    public List<Payment> findByOrderId(String orderId) {

        return repository.findByOrderId(orderId)
                .stream()
                .map(PaymentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void updateStatusAndProviderData(

            String paymentId,
            PaymentStatus status,
            String transactionId,
            String provider,
            Instant paid,
            Instant failedAt,
            Instant refundedAt) {

        //TODO
    }
}
