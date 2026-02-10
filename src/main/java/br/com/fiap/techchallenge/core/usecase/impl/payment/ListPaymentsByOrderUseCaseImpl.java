package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.ListPaymentsByOrderUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.util.List;

public class ListPaymentsByOrderUseCaseImpl implements ListPaymentsByOrderUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;

    public ListPaymentsByOrderUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<PaymentView> execute(String orderId) {

        orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        List<Payment> list = paymentRepository.findByOrderId(orderId);

        return list.stream().map(this::toView).toList();
    }

    private PaymentView toView(Payment p) {
        return new PaymentView(
                p.getId(), p.getOrderId(), p.getCreatedAt(),
                p.getAmount(), p.getMethod(), p.getStatus(),
                p.getTransactionId(), p.getProvider(),
                p.getPaidAt(), p.getFailedAt(), p.getRefundedAt()
        );
    }
}
