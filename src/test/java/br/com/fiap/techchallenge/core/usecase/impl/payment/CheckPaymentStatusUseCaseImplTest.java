package br.com.fiap.techchallenge.core.usecase.impl.payment;

import br.com.fiap.techchallenge.core.domain.enums.PaymentMethod;
import br.com.fiap.techchallenge.core.domain.enums.PaymentStatus;
import br.com.fiap.techchallenge.core.domain.model.Payment;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsFailedUseCase;
import br.com.fiap.techchallenge.core.usecase.in.payment.status.MarkPaymentAsPaidUseCase;
import br.com.fiap.techchallenge.core.usecase.out.PaymentRepositoryPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.ExternalPaymentGatewayPort;
import br.com.fiap.techchallenge.core.usecase.out.external_payment.dto.ExternalPaymentStatusResult;
import br.com.fiap.techchallenge.core.usecase.out.messaging.PaymentEventPublisherPort;
import br.com.fiap.techchallenge.core.usecase.out.messaging.dto.PaymentStatusCheckMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckPaymentStatusUseCaseImplTest {

    @Mock
    private PaymentRepositoryPort paymentRepository;

    @Mock
    private ExternalPaymentGatewayPort externalPaymentGateway;

    @Mock
    private MarkPaymentAsPaidUseCase markPaymentAsPaidUseCase;

    @Mock
    private MarkPaymentAsFailedUseCase markPaymentAsFailedUseCase;

    @Mock
    private PaymentEventPublisherPort paymentEventPublisher;

    private CheckPaymentStatusUseCaseImpl useCase;

    private static final long BASE_DELAY_MS = 10_000;
    private static final int MAX_ATTEMPTS = 6;
    private static final String PAYMENT_ID = "pay-123";
    private static final String ORDER_ID = "order-456";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CheckPaymentStatusUseCaseImpl(
                paymentRepository,
                externalPaymentGateway,
                markPaymentAsPaidUseCase,
                markPaymentAsFailedUseCase,
                paymentEventPublisher,
                BASE_DELAY_MS,
                MAX_ATTEMPTS
        );
    }

    private Payment createPendingPayment() {
        return new Payment(
                PAYMENT_ID, ORDER_ID, Instant.now(),
                BigDecimal.valueOf(100), PaymentMethod.PIX, PaymentStatus.PENDING,
                null, null, null, null, null
        );
    }

    private PaymentStatusCheckMessage createMessage(int attempt) {
        return new PaymentStatusCheckMessage(PAYMENT_ID, ORDER_ID, attempt, Instant.now());
    }

    @Test
    void shouldMarkPaymentAsPaidWhenGatewayReturnsPago() {
        Payment payment = createPendingPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(externalPaymentGateway.getPaymentStatus(PAYMENT_ID))
                .thenReturn(new ExternalPaymentStatusResult(PAYMENT_ID, "pago"));

        useCase.execute(createMessage(1));

        verify(markPaymentAsPaidUseCase).execute(ORDER_ID, PAYMENT_ID);
        verify(paymentRepository).updateStatusAndProviderData(
                eq(PAYMENT_ID), eq(PaymentStatus.PAID), anyString(), anyString(),
                any(Instant.class), isNull(), isNull()
        );
        verifyNoInteractions(markPaymentAsFailedUseCase);
        verifyNoInteractions(paymentEventPublisher);
    }

    @Test
    void shouldMarkPaymentAsFailedWhenGatewayReturnsRejeitado() {
        Payment payment = createPendingPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(externalPaymentGateway.getPaymentStatus(PAYMENT_ID))
                .thenReturn(new ExternalPaymentStatusResult(PAYMENT_ID, "rejeitado"));

        useCase.execute(createMessage(2));

        verify(markPaymentAsFailedUseCase).execute(ORDER_ID, PAYMENT_ID);
        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(paymentEventPublisher);
    }

    @Test
    void shouldRescheduleWhenStatusIsStillPendingAndBelowMaxAttempts() {
        Payment payment = createPendingPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(externalPaymentGateway.getPaymentStatus(PAYMENT_ID))
                .thenReturn(new ExternalPaymentStatusResult(PAYMENT_ID, "pendente"));

        useCase.execute(createMessage(2));

        ArgumentCaptor<PaymentStatusCheckMessage> msgCaptor = ArgumentCaptor.forClass(PaymentStatusCheckMessage.class);
        ArgumentCaptor<Long> delayCaptor = ArgumentCaptor.forClass(Long.class);

        verify(paymentEventPublisher).publishPaymentStatusCheck(msgCaptor.capture(), delayCaptor.capture());

        assertEquals(3, msgCaptor.getValue().attemptNumber());
        assertEquals(PAYMENT_ID, msgCaptor.getValue().paymentId());

        long expectedDelay = BASE_DELAY_MS * (long) Math.pow(2, 2);
        assertEquals(expectedDelay, delayCaptor.getValue());

        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(markPaymentAsFailedUseCase);
    }

    @Test
    void shouldSendToExpiredQueueWhenMaxAttemptsReached() {
        Payment payment = createPendingPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(externalPaymentGateway.getPaymentStatus(PAYMENT_ID))
                .thenReturn(new ExternalPaymentStatusResult(PAYMENT_ID, "pendente"));

        useCase.execute(createMessage(MAX_ATTEMPTS));

        verify(paymentEventPublisher).publishPaymentExpired(any(PaymentStatusCheckMessage.class));
        verify(paymentEventPublisher, never()).publishPaymentStatusCheck(any(), anyLong());
        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(markPaymentAsFailedUseCase);
    }

    @Test
    void shouldSkipWhenPaymentIsNoLongerPending() {
        Payment paidPayment = new Payment(
                PAYMENT_ID, ORDER_ID, Instant.now(),
                BigDecimal.valueOf(100), PaymentMethod.PIX, PaymentStatus.PAID,
                "tx-1", "provider", Instant.now(), null, null
        );
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(paidPayment));

        useCase.execute(createMessage(1));

        verifyNoInteractions(externalPaymentGateway);
        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(markPaymentAsFailedUseCase);
        verifyNoInteractions(paymentEventPublisher);
    }

    @Test
    void shouldSkipWhenPaymentNotFound() {
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.empty());

        useCase.execute(createMessage(1));

        verifyNoInteractions(externalPaymentGateway);
        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(markPaymentAsFailedUseCase);
        verifyNoInteractions(paymentEventPublisher);
    }

    @Test
    void shouldRescheduleWhenGatewayThrowsException() {
        Payment payment = createPendingPayment();
        when(paymentRepository.findById(PAYMENT_ID)).thenReturn(Optional.of(payment));
        when(externalPaymentGateway.getPaymentStatus(PAYMENT_ID))
                .thenThrow(new RuntimeException("Gateway timeout"));

        useCase.execute(createMessage(1));

        verify(paymentEventPublisher).publishPaymentStatusCheck(any(PaymentStatusCheckMessage.class), anyLong());
        verifyNoInteractions(markPaymentAsPaidUseCase);
        verifyNoInteractions(markPaymentAsFailedUseCase);
    }
}
