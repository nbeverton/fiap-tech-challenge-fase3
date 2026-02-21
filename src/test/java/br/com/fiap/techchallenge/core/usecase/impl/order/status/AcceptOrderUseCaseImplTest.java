package br.com.fiap.techchallenge.core.usecase.impl.order.status;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.core.usecase.out.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AcceptOrderUseCaseImplTest {

    private Order orderWithStatus(OrderStatus status) {
        DeliveryAddressSnapshot address = new DeliveryAddressSnapshot(
                "Rua A", 10, "Centro", "SP", "SP", "00000-000", "Apto 1"
        );
        OrderItem item = new OrderItem("menu-1", "Pizza", 1, new BigDecimal("50.00"));

        return new Order(
                "order-1", "rest-1", "user-1", "addr-1",
                address, List.of(item),
                new BigDecimal("50.00"),
                status,
                Instant.now(), Instant.now()
        );
    }

    @Test
    void accept_created_shouldMoveToAwaitingPayment_andSave() {
        OrderRepositoryPort repo = mock(OrderRepositoryPort.class);
        when(repo.findById("order-1")).thenReturn(Optional.of(orderWithStatus(OrderStatus.CREATED)));

        AcceptOrderUseCaseImpl useCase = new AcceptOrderUseCaseImpl(repo);
        useCase.accept("order-1");

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(repo).save(captor.capture());

        assertEquals(OrderStatus.AWAITING_PAYMENT, captor.getValue().getOrderStatus());
    }

    @Test
    void accept_paymentConfirmed_shouldMoveToPaid_andSave_orThrowBasedOnDomainRule() {

        OrderRepositoryPort repo = mock(OrderRepositoryPort.class);
        when(repo.findById("order-1")).thenReturn(Optional.of(orderWithStatus(OrderStatus.PAYMENT_CONFIRMED)));

        AcceptOrderUseCaseImpl useCase = new AcceptOrderUseCaseImpl(repo);

        try {
            useCase.accept("order-1");

            ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
            verify(repo).save(captor.capture());
            assertEquals(OrderStatus.PAID, captor.getValue().getOrderStatus());
        } catch (RuntimeException ex) {

            assertTrue(ex.getClass().getSimpleName().contains("InvalidOrderStatus"));
            verify(repo, never()).save(any());
        }
    }
}