package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpirePaymentUseCaseImplTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @Mock
    private OrderRepositoryPort orderRepository;

    private ExpirePaymentUseCaseImpl useCase;

    private static final String PAYMENT_ID = "pay-123";
    private static final String ORDER_ID = "order-456";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ExpirePaymentUseCaseImpl(paymentRepository, orderRepository);
    }

    private Payment createPendingPayment() {
        return new Payment(
                PAYMENT_ID, ORDER_ID, Instant.now(),
                BigDecimal.valueOf(100), PaymentMethod.PIX, PaymentStatus.PENDING,
                null, null, null, null, null
        );
    }

    private Order createOrder(OrderStatus status) {
        return new Order(
                ORDER_ID, "rest-1", "user-1", "addr-1",
                null, Collections.emptyList(), BigDecimal.valueOf(100),
                status, Instant.now(), Instant.now()
        );
    }

    private PaymentStatusCheckMessage createMessage() {
        return new PaymentStatusCheckMessage(PAYMENT_ID, ORDER_ID, 6, Instant.now());
    }

    @Test
    void shouldMarkPaymentAsFailedAndCancelOrderWhenAwaitingPayment() {
        Payment payment = createPendingPayment();
        Order order = createOrder(OrderStatus.AWAITING_PAYMENT);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        useCase.execute(createMessage());

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertEquals(PaymentStatus.FAILED, paymentCaptor.getValue().getStatus());

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELED, orderCaptor.getValue().getOrderStatus());
    }

    @Test
    void shouldMarkPaymentAsFailedAndCancelOrderWhenCreated() {
        Payment payment = createPendingPayment();
        Order order = createOrder(OrderStatus.CREATED);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        useCase.execute(createMessage());

        verify(paymentRepository).save(any(Payment.class));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(OrderStatus.CANCELED, orderCaptor.getValue().getOrderStatus());
    }

    @Test
    void shouldMarkPaymentAsFailedButNotCancelOrderWhenAlreadyPaid() {
        Payment payment = createPendingPayment();
        Order order = createOrder(OrderStatus.PAID);

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        useCase.execute(createMessage());

        verify(paymentRepository).save(any(Payment.class));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldSkipWhenPaymentAlreadyFailed() {
        Payment failedPayment = new Payment(
                PAYMENT_ID, ORDER_ID, Instant.now(),
                BigDecimal.valueOf(100), PaymentMethod.PIX, PaymentStatus.FAILED,
                null, null, null, Instant.now(), null
        );

        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(failedPayment));

        useCase.execute(createMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(orderRepository);
    }

    @Test
    void shouldSkipWhenPaymentNotFound() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.empty());

        useCase.execute(createMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
        verifyNoInteractions(orderRepository);
    }
}
