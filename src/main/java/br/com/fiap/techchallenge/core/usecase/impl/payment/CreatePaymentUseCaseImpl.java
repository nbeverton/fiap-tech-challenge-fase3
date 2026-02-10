package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.CreatePaymentCommand;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.time.Instant;
import java.util.UUID;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;

    public CreatePaymentUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public PaymentView execute(CreatePaymentCommand command) {

        if (command == null) {
            throw new InvalidPaymentException("command must not be null");
        }
        if (command.orderId() == null || command.orderId().isBlank()) {
            throw new InvalidPaymentException("orderId must not be null or blank");
        }
        if (command.method() == null) {
            throw new InvalidPaymentException("method must not be null");
        }
        if (command.amount() == null) {
            throw new InvalidPaymentException("amount must not be null");
        }

        orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                command.orderId(),
                Instant.now(),
                command.amount(),
                command.method(),
                PaymentStatus.PENDING,
                null,
                null,
                null,
                null,
                null
        );

        paymentRepository.save(payment);

        return new PaymentView(
                payment.getId(),
                payment.getOrderId(),
                payment.getCreatedAt(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getProvider(),
                payment.getPaidAt(),
                payment.getFailedAt(),
                payment.getRefundedAt()
        );
    }
}
