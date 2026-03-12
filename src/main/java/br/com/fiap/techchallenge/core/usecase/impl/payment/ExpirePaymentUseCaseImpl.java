package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.ExpirePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ExpirePaymentUseCaseImpl implements ExpirePaymentUseCase {

    private static final Logger log = LoggerFactory.getLogger(ExpirePaymentUseCaseImpl.class);

    private static final Set<OrderStatus> CANCELLABLE_STATUSES = Set.of(
            OrderStatus.CREATED,
            OrderStatus.AWAITING_PAYMENT
    );

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;

    public ExpirePaymentUseCaseImpl(PaymentRepositoryPort paymentRepository,
                                    OrderRepositoryPort orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(PaymentStatusCheckMessage message) {

        Payment payment = paymentRepository.findById(message.paymentId()).orElse(null);

        if (payment == null) {
            log.warn("Payment {} not found during expiration", message.paymentId());
            return;
        }

        if (payment.getStatus() == PaymentStatus.PENDING) {
            payment.markAsFailed();
            paymentRepository.save(payment);
            log.info("Payment {} expired and marked as FAILED", message.paymentId());
        } else {
            log.info("Payment {} already in status {}, skipping expiration",
                    message.paymentId(), payment.getStatus());
            return;
        }

        Order order = orderRepository.findById(message.orderId()).orElse(null);

        if (order != null && CANCELLABLE_STATUSES.contains(order.getOrderStatus())) {
            order.markOrderAsCancel();
            orderRepository.save(order);
            log.info("Order {} cancelled due to payment {} expiration",
                    message.orderId(), message.paymentId());
        }
    }
}
