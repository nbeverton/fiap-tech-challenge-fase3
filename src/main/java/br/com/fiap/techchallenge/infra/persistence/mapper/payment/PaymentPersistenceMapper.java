package br.com.fiap.techchallenge.infra.persistence.mapper.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.infra.persistence.documents.PaymentDocument;

public class PaymentPersistenceMapper {


    public static PaymentDocument toDocument(Payment domain){

        PaymentDocument document = new PaymentDocument();

                document.setId(domain.getId());
                document.setOrderId(domain.getOrderId());
                document.setAmount(domain.getAmount());
                document.setMethod(domain.getMethod().name());
                document.setStatus(domain.getStatus().name());
                document.setTransactionId(domain.getTransactionId());
                document.setProvider(domain.getProvider());
                document.setPaidAt(domain.getPaidAt());
                document.setFailedAt(domain.getFailedAt());
                document.setRefundedAt(domain.getRefundedAt());

                return document;
    }


    public static Payment toDomain(PaymentDocument document){

        return new Payment(

                document.getId(),
                document.getOrderId(),
                document.getCreatedAt(),
                document.getAmount(),
                PaymentMethod.valueOf(document.getMethod()),
                PaymentStatus.valueOf(document.getStatus()),
                document.getTransactionId(),
                document.getProvider(),
                document.getPaidAt(),
                document.getFailedAt(),
                document.getRefundedAt()
        );
    }
}
