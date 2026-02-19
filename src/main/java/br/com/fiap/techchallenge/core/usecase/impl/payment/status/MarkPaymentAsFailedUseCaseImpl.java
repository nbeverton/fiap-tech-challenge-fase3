package br.com.fiap.techchallenge.core.usecase.impl.payment.status;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentStatusException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentOrderMismatchException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.time.Instant;

public class MarkPaymentAsFailedUseCaseImpl implements MarkPaymentAsFailedUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public MarkPaymentAsFailedUseCaseImpl(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Override
    public void execute(String orderId, String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(()-> new PaymentNotFoundException(paymentId));

        if (!payment.getOrderId().equals(orderId)) {
            throw new PaymentOrderMismatchException("Payment does not belong to this order");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentStatusException("Only PENDING payments can be marked as FAILED");
        }

        payment.markAsFailed();

        paymentRepository.save(payment);
    }
}
