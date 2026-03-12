package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.CheckPaymentStatusUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentStatusResult;
import br.com.fiap.techchallenge.core.usecase.out.messaging.PaymentEventPublisherPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class CheckPaymentStatusUseCaseImpl implements CheckPaymentStatusUseCase {

    private static final Logger log = LoggerFactory.getLogger(CheckPaymentStatusUseCaseImpl.class);
    private static final String EXTERNAL_PAYMENT_PROVIDER = "PAGAMENTO_EXTERNO_FIAP";

    private final PaymentRepositoryPort paymentRepository;
    private final ExternalPaymentGatewayPort externalPaymentGateway;
    private final MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;
    private final MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase;
    private final PaymentEventPublisherPort paymentEventPublisher;
    private final long baseDelayMs;
    private final int maxAttempts;

    public CheckPaymentStatusUseCaseImpl(
            PaymentRepositoryPort paymentRepository,
            ExternalPaymentGatewayPort externalPaymentGateway,
            MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase,
            MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase,
            PaymentEventPublisherPort paymentEventPublisher,
            long baseDelayMs,
            int maxAttempts) {

        this.paymentRepository = paymentRepository;
        this.externalPaymentGateway = externalPaymentGateway;
        this.markPaymentAsPaidUseCase = markPaymentAsPaidUseCase;
        this.markPaymentAsFailedUseCase = markPaymentAsFailedUseCase;
        this.paymentEventPublisher = paymentEventPublisher;
        this.baseDelayMs = baseDelayMs;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void execute(PaymentStatusCheckMessage message) {

        Payment payment = paymentRepository.findById(message.paymentId()).orElse(null);

        if (payment == null || payment.getStatus() != PaymentStatus.PENDING) {
            log.info("Payment {} is no longer PENDING, skipping", message.paymentId());
            return;
        }

        try {
            ExternalPaymentStatusResult statusResult =
                    externalPaymentGateway.getPaymentStatus(message.paymentId());

            String rawStatus = statusResult.rawStatus();

            if ("pago".equalsIgnoreCase(rawStatus)) {
                markPaymentAsPaidUseCase.execute(message.orderId(), message.paymentId());

                paymentRepository.updateStatusAndProviderData(
                        message.paymentId(),
                        PaymentStatus.PAID,
                        UUID.randomUUID().toString(),
                        EXTERNAL_PAYMENT_PROVIDER,
                        Instant.now(),
                        null,
                        null
                );

                log.info("Payment {} confirmed as PAID on attempt {}", message.paymentId(), message.attemptNumber());

            } else if ("rejeitado".equalsIgnoreCase(rawStatus)) {
                markPaymentAsFailedUseCase.execute(message.orderId(), message.paymentId());
                log.info("Payment {} rejected on attempt {}", message.paymentId(), message.attemptNumber());

            } else {
                rescheduleOrExpire(message);
            }

        } catch (Exception ex) {
            log.error("Error checking payment {} status on attempt {}: {}",
                    message.paymentId(), message.attemptNumber(), ex.getMessage());
            rescheduleOrExpire(message);
        }
    }

    private void rescheduleOrExpire(PaymentStatusCheckMessage message) {
        if (message.attemptNumber() >= maxAttempts) {
            paymentEventPublisher.publishPaymentExpired(message);
            log.info("Payment {} reached max attempts ({}), sending to expired queue",
                    message.paymentId(), maxAttempts);
        } else {
            int nextAttempt = message.attemptNumber() + 1;
            long nextDelay = calculateDelay(nextAttempt);

            PaymentStatusCheckMessage nextMessage = new PaymentStatusCheckMessage(
                    message.paymentId(),
                    message.orderId(),
                    nextAttempt,
                    message.createdAt()
            );

            paymentEventPublisher.publishPaymentStatusCheck(nextMessage, nextDelay);
            log.info("Payment {} rescheduled: attempt={}, delay={}ms",
                    message.paymentId(), nextAttempt, nextDelay);
        }
    }

    private long calculateDelay(int attemptNumber) {
        return baseDelayMs * (long) Math.pow(2, attemptNumber - 1);
    }
}
