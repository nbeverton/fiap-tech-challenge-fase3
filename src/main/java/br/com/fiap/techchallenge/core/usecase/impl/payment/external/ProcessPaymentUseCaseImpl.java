package br.com.fiap.techchallenge.core.usecase.impl.payment.external;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.event.PaymentApprovedEventPublisherPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentRequest;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentResponse;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentStatusResult;
import br.com.fiap.techchallenge.infra.messaging.kafka.event.PaymentApprovedEvent;

import java.time.Instant;
import java.util.UUID;

public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private static final String EXTERNAL_PAYMENT_PROVIDER = "PAGAMENTO_EXTERNO_FIAP";

    private final ExternalPaymentProcessor externalPaymentProcessor;
    private final PaymentRepositoryPort paymentRepository;
    private final ExternalPaymentGatewayPort externalPaymentGateway;
    private final MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;
    private final PaymentApprovedEventPublisherPort paymentApprovedEventPublisher;

    public ProcessPaymentUseCaseImpl(ExternalPaymentProcessor externalPaymentProcessor, PaymentRepositoryPort paymentRepository, ExternalPaymentGatewayPort externalPaymentGateway, MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase, PaymentApprovedEventPublisherPort paymentApprovedEventPublisher) {
        this.externalPaymentProcessor = externalPaymentProcessor;
        this.paymentRepository = paymentRepository;
        this.externalPaymentGateway = externalPaymentGateway;
        this.markPaymentAsPaidUseCase = markPaymentAsPaidUseCase;
        this.paymentApprovedEventPublisher = paymentApprovedEventPublisher;
    }


    @Override
    public void execute(Order order, Payment payment) {

        ExternalPaymentResponse externalPaymentResult = externalPaymentProcessor.submitPayment(
                new ExternalPaymentRequest(
                        payment.getAmount(),
                        payment.getId(),
                        order.getUserId()
                ),
                order.getId(),
                payment.getId()
        ).join();

        if (!externalPaymentResult.accepted()) {
            return;
        }

        paymentRepository.updateStatusAndProviderData(
                payment.getId(),
                PaymentStatus.PENDING,
                null,
                EXTERNAL_PAYMENT_PROVIDER,
                null,
                null,
                null
        );

        ExternalPaymentStatusResult statusResult = externalPaymentGateway.getPaymentStatus(payment.getId());

        if ("pago".equalsIgnoreCase(statusResult.rawStatus())) {

            markPaymentAsPaidUseCase.execute(order.getId(), payment.getId());

            paymentRepository.updateStatusAndProviderData(
                    payment.getId(),
                    PaymentStatus.PAID,
                    UUID.randomUUID().toString(),
                    EXTERNAL_PAYMENT_PROVIDER,
                    Instant.now(),
                    null,
                    null
            );

            paymentApprovedEventPublisher.publish(
                    new PaymentApprovedEvent(
                            UUID.randomUUID().toString(),
                            "pagamento.aprovado",
                            Instant.now(),
                            payment.getId(),
                            order.getId()
                    )
            );
        }
    }
}
