package br.com.fiap.techchallenge.core.usecase.impl.payment.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentStatusException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentOrderMismatchException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsRefundedUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

public class MarkPaymentAsRefundedUseCaseImpl implements MarkPaymentAsRefundedUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;

    public MarkPaymentAsRefundedUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void execute(String orderId, String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));


        if(!payment.getOrderId().equals(orderId)){
            throw new PaymentOrderMismatchException("Payment does not belong to this order");
        }

        if(payment.getStatus() != PaymentStatus.PAID){
            throw new InvalidPaymentStatusException("Only PAID payments can be refunded");
        }

        payment.markAsRefunded();

        paymentRepository.save(payment);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if(order.getOrderStatus() == OrderStatus.PAID){

            order.markOrderAsAwaitPayment();
            orderRepository.save(order);
        }
    }
}
