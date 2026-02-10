package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundAfterUpdateException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.UpdatePaymentStatusUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.UpdatePaymentStatusCommand;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.time.Instant;

public class UpdatePaymentStatusUseCaseImpl implements UpdatePaymentStatusUseCase {

    private final PaymentRepositoryPort paymentRepository;

    public UpdatePaymentStatusUseCaseImpl(PaymentRepositoryPort paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentView  execute(UpdatePaymentStatusCommand command) {

        if (command == null) {
            throw new InvalidPaymentException("command must not be null");
        }
        if (command.paymentId() == null || command.paymentId().isBlank()) {
            throw new InvalidPaymentException("paymentId must not be null or blank");
        }
        if (command.newStatus() == null) {
            throw new InvalidPaymentException("newStatus must not be null");
        }

        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(()-> new PaymentNotFoundException(command.paymentId()));

        // valida transição
        validateTransition(payment.getStatus(), command.newStatus());

        Instant occurredAt = (command.occurredAt() != null) ? command.occurredAt() : Instant.now();

        Instant paidAt = null;
        Instant failedAt = null;
        Instant refundedAt = null;

        if (command.newStatus() == PaymentStatus.PAID) {
            paidAt = occurredAt;
        } else if (command.newStatus() == PaymentStatus.FAILED) {
            failedAt = occurredAt;
        } else if (command.newStatus() == PaymentStatus.REFUNDED) {
            refundedAt = occurredAt;
        }

        paymentRepository.updateStatusAndProviderData(
                command.paymentId(),
                command.newStatus(),
                command.transactionId(),
                command.provider(),
                paidAt,
                failedAt,
                refundedAt
        );

        // Rebusca para retornar exatamente o que ficou persistido
        Payment updated = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new PaymentNotFoundAfterUpdateException(command.paymentId()));

        return toView(updated);
    }

    private void validateTransition(PaymentStatus from, PaymentStatus to) {
        // idempotência: se já estiver no status, não precisa explodir
        if (from == to) return;

        // Regras básicas:
        // PENDING -> PAID | FAILED
        // PAID -> REFUNDED
        // FAILED -> (não muda; nova tentativa = novo Payment)
        // REFUNDED -> (não muda)
        switch (from) {
            case PENDING -> {
                if (to != PaymentStatus.PAID && to != PaymentStatus.FAILED) {
                    throw new InvalidPaymentException("invalid status transition: " + from + " -> " + to);
                }
            }
            case PAID -> {
                if (to != PaymentStatus.REFUNDED) {
                    throw new InvalidPaymentException("invalid status transition: " + from + " -> " + to);
                }
            }
            case FAILED, REFUNDED -> throw new InvalidPaymentException("cannot change status from " + from);
        }
    }

    private PaymentView toView(Payment p) {
        return new PaymentView(
                p.getId(),
                p.getOrderId(),
                p.getCreatedAt(),
                p.getAmount(),
                p.getMethod(),
                p.getStatus(),
                p.getTransactionId(),
                p.getProvider(),
                p.getPaidAt(),
                p.getFailedAt(),
                p.getRefundedAt()
        );
    }
}

