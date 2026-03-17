package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.OrderNotFoundException;
import br.com.fiap.techchallenge.core.domain.exception.payment.InvalidPaymentException;
import br.com.fiap.techchallenge.core.domain.exception.payment.OverpaymentException;
import br.com.fiap.techchallenge.core.domain.exception.payment.PendingPaymentAlreadyExistsException;
import br.com.fiap.techchallenge.core.domain.exception.security.ForbiddenException;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.domain.security.AuthContext;
import br.com.fiap.techchallenge.core.usecase.in.payment.CreatePaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.external.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.CreatePaymentCommand;
import br.com.fiap.techchallenge.core.usecase.in.payment.dto.PaymentView;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

    private final PaymentRepositoryPort paymentRepository;
    private final OrderRepositoryPort orderRepository;
    private final ProcessPaymentUseCase processPaymentUseCase;
    private final MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;

    public CreatePaymentUseCaseImpl(PaymentRepositoryPort paymentRepository, OrderRepositoryPort orderRepository, ProcessPaymentUseCase processPaymentUseCase, MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.processPaymentUseCase = processPaymentUseCase;
        this.markPaymentAsPaidUseCase = markPaymentAsPaidUseCase;
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
            throw new InvalidPaymentException("valor must not be null");
        }

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException(command.orderId()));

        List<Payment> paymentsFromOrder = paymentRepository.findByOrderId(order.getId());

        for(Payment p : paymentsFromOrder){

            if(
                    p.getStatus() == PaymentStatus.PENDING &&
                    p.getFailedAt() != null &&
                    p.getPaidAt() == null &&
                    p.getTransactionId() == null &&
                    p.getProvider() == null
            ){
                throw new PendingPaymentAlreadyExistsException();
            }
        }

        // Authorization: ADMIN can pay any order; CLIENT can pay only own order
        if (!AuthContext.isAdmin()) {

            if (!AuthContext.isClient()) {
                throw new ForbiddenException("Forbidden: only CLIENT can create payments");
            }

            String requesterUserId = AuthContext.userId();
            if (!order.getUserId().equals(requesterUserId)) {
                throw new ForbiddenException("Forbidden: you can only create payments for your own orders");
            }
        }

        doesPaymentExceedBalance(order, command.amount());

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                command.orderId(),
                Instant.now(),
                command.amount(),
                command.method(),
                PaymentStatus.PENDING,
                null,  // transactionId
                null,  // provider
                null,  // paidAt
                null,  // failedAt
                null   // refundedAt
        );

        paymentRepository.save(payment);

        // ✅ aqui ele vira PAID e o order é ajustado conforme sua regra
        markPaymentAsPaidUseCase.execute(order.getId(), payment.getId());

        Payment updatePayment = paymentRepository.findById(payment.getId())
                .orElse(payment);

        return new PaymentView(
                updatePayment.getId(),
                updatePayment.getOrderId(),
                updatePayment.getCreatedAt(),
                updatePayment.getAmount(),
                updatePayment.getMethod(),
                updatePayment.getStatus(),
                updatePayment.getTransactionId(),
                updatePayment.getProvider(),
                updatePayment.getPaidAt(),
                updatePayment.getFailedAt(),
                updatePayment.getRefundedAt(),
                buildPaymentMessage(updatePayment.getStatus())
        );
    }

    private String buildPaymentMessage(PaymentStatus status) {
        return switch (status) {
            case PAID -> "Payment processed successfully.";
            case FAILED -> "Payment was created, but external payment processing failed.";
            case PENDING -> "Payment was created and is pending external processing.";
            case REFUNDED -> "Payment was refunded successfully.";
        };
    }



    private void doesPaymentExceedBalance(Order order, BigDecimal amount){

        List<Payment> payments = paymentRepository.findByOrderId(order.getId());

        BigDecimal totalPaid = payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PAID)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = order.getTotalAmount().subtract(totalPaid);

        if (remaining.compareTo(amount) < 0) {
            throw new OverpaymentException("Payment exceeds remaining balance. Remaining valor: " + remaining);
        }
    }
}