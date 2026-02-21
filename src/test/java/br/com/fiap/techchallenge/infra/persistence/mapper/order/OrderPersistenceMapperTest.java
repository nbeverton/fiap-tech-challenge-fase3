package br.com.fiap.techchallenge.infra.persistence.mapper.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.DeliveryAddressSnapshotEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.OrderItemEmbedded;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderPersistenceMapperTest {

    @Test
    void toDocument_and_back_shouldPreserveStatusAndItems() {
        DeliveryAddressSnapshot address = new DeliveryAddressSnapshot(
                "Rua A", 10, "Centro", "SP", "SP", "00000-000", "Apto 1"
        );

        OrderItem item = new OrderItem("menu-1", "Pizza", 2, new BigDecimal("50.00"));

        Order order = new Order(
                "order-1", "rest-1", "user-1", "addr-1",
                address, List.of(item),
                new BigDecimal("100.00"),
                OrderStatus.AWAITING_PAYMENT,
                Instant.now(), Instant.now()
        );

        OrderDocument doc = OrderPersistenceMapper.toDocument(order);
        assertEquals("AWAITING_PAYMENT", doc.getOrderStatus());

        Order back = OrderPersistenceMapper.toDomain(doc);
        assertEquals(OrderStatus.AWAITING_PAYMENT, back.getOrderStatus());

        assertEquals(1, back.getItems().size());
        assertEquals("menu-1", back.getItems().get(0).getMenuItemId());
        assertEquals(2, back.getItems().get(0).getQuantity());
    }
}