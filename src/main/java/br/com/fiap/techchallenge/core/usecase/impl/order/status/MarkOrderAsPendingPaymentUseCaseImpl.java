package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentOrderMismatchException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.order.status.MarkOrderAsPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.time.Instant;

public class MarkOrderAsPendingPaymentUseCaseImpl implements MarkOrderAsPendingPaymentUseCase {

    private final OrderRepositoryPort orderRepository;
    private final PaymentRepositoryPort paymentRepository;


    public MarkOrderAsPendingPaymentUseCaseImpl(OrderRepositoryPort orderRepository, PaymentRepositoryPort paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void execute(String orderId, String paymentId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if(!existingPayment.getOrderId().equals(orderId)){
            throw new PaymentOrderMismatchException();
        }

        if(existingPayment.getStatus() != PaymentStatus.PENDING){
            throw new InvalidPaymentException(
                    "Only PENDING payments can be marked as pending external processing."
            );
        }

        order.markOrderAsAwaitPayment();

        orderRepository.save(order);

        Payment payment = new Payment(
                existingPayment.getId(),
                existingPayment.getOrderId(),
                existingPayment.getCreatedAt(),
                existingPayment.getAmount(),
                existingPayment.getMethod(),
                PaymentStatus.PENDING,
                null,
                null,
                null,
                Instant.now(),
                existingPayment.getRefundedAt()
        );

        paymentRepository.save(payment);
    }
}
