package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PaymentNotFoundException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.ReprocessPendingPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

public class ReprocessPendingPaymentUseCaseImpl implements ReprocessPendingPaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepositoryPort;
    private final ProcessPaymentUseCase processPaymentUseCase;

    public ReprocessPendingPaymentUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepositoryPort, ProcessPaymentUseCase processPaymentUseCase) {
        this.paymentRepository = paymentRepository;
        this.orderRepositoryPort = orderRepositoryPort;
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @Override
    public void execute(String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        Order order = orderRepositoryPort.findById(payment.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(payment.getOrderId()));

        processPaymentUseCase.execute(order, payment);
    }
}
