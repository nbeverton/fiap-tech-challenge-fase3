package br.com.fiap.techchallenge.core.usecase.impl.payment.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentStatusException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentOrderMismatchException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class MarkPaymentAsPaidUseCaseImpl implements MarkPaymentAsPaidUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;

    public MarkPaymentAsPaidUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepository) {
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

        if(payment.getStatus() != PaymentStatus.PENDING){
            throw new InvalidPaymentStatusException("Only PENDING payments can be marked as PAID");
        }

        payment.markAsPaid();

        paymentRepository.save(payment);


        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(payment.getOrderId()));

        List<Payment> payments = paymentRepository.findByOrderId(order.getId());

        BigDecimal totalPaid = payments.stream()
                .filter(p->p.getStatus() == PaymentStatus.PAID)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        BigDecimal remaining = order.getTotalAmount().subtract(totalPaid);

        if(remaining.compareTo(BigDecimal.ZERO) <= 0){

            if(order.getOrderStatus() == OrderStatus.AWAITING_PAYMENT){

                order.markOrderAsPaid();
                orderRepository.save(order);
            }

            else if(order.getOrderStatus() == OrderStatus.CREATED){

                order.markPaymentAsConfirmed();
                orderRepository.save(order);
            }
        }
    }
}
