package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTransitionsTest {

    private Order newOrderWithStatus(OrderStatus status) {
        DeliveryAddressSnapshot address = new DeliveryAddressSnapshot(
                "Rua A", 10, "Centro", "SP", "SP", "00000-000", "Apto 1"
        );

        OrderItem item = new OrderItem(
                "menu-1", "Pizza", 1, new BigDecimal("50.00")
        );

        return new Order(
                "order-1",
                "rest-1",
                "user-1",
                "addr-1",
                address,
                List.of(item),
                new BigDecimal("50.00"),
                status,
                Instant.now(),
                Instant.now()
        );
    }

    @Test
    void created_to_awaitingPayment_ok() {
        Order order = newOrderWithStatus(OrderStatus.CREATED);

        order.markOrderAsAwaitPayment();

        assertEquals(OrderStatus.AWAITING_PAYMENT, order.getOrderStatus());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    void created_to_paymentConfirmed_ok() {
        Order order = newOrderWithStatus(OrderStatus.CREATED);

        order.markPaymentAsConfirmed();

        assertEquals(OrderStatus.PAYMENT_CONFIRMED, order.getOrderStatus());
    }

    @Test
    void awaitingPayment_to_paid_ok() {
        Order order = newOrderWithStatus(OrderStatus.AWAITING_PAYMENT);

        order.markOrderAsPaid();

        assertEquals(OrderStatus.PAID, order.getOrderStatus());
    }

    @Test
    void created_to_paid_shouldThrow() {
        Order order = newOrderWithStatus(OrderStatus.CREATED);

        assertThrows(InvalidOrderStatusException.class, order::markOrderAsPaid);
    }

    @Test
    void cancel_delivered_shouldThrow() {
        Order order = newOrderWithStatus(OrderStatus.DELIVERED);

        assertThrows(InvalidOrderStatusException.class, order::markOrderAsCancel);
    }

    @Test
    void cancel_twice_shouldBeIdempotent() {
        Order order = newOrderWithStatus(OrderStatus.CREATED);

        order.markOrderAsCancel();
        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());

        order.markOrderAsCancel();
        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());
    }
}