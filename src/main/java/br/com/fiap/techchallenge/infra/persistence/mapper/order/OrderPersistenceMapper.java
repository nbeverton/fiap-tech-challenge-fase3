package br.com.fiap.techchallenge.infra.persistence.mapper.order;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;
import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.DeliveryAddressSnapshotEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.OrderItemEmbedded;

public class OrderPersistenceMapper {

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId());
        doc.setRestaurantId(order.getRestaurantId());
        doc.setUserId(order.getUserId());

        doc.setUserAddressId(order.getUserAddressId());

        doc.setDeliveryAddress(toEmbedded(order.getDeliveryAddress()));

        doc.setItems
                (order.getItems().stream()
                        .map(OrderPersistenceMapper::toEmbedded)
                        .toList()
        );

        doc.setOrderStatus(order.getOrderStatus().name());
        doc.setTotalAmount(order.getTotalAmount());
        doc.setCreatedAt(order.getCreatedAt());
        doc.setUpdatedAt(order.getUpdatedAt());
        return doc;
    }

    public static Order toDomain(OrderDocument doc) {
        return new Order(
                doc.getId(),
                doc.getRestaurantId(),
                doc.getUserId(),
                doc.getUserAddressId(),
                toDomain(doc.getDeliveryAddress()),

                doc.getItems().stream()
                        .map(OrderPersistenceMapper::toDomain)
                        .toList(),

                doc.getTotalAmount(),
                OrderStatus.valueOf(doc.getOrderStatus()),
                doc.getCreatedAt(),
                doc.getUpdatedAt()
        );
    }


    // =============================
    // Embedded Converters
    // =============================
    private static DeliveryAddressSnapshotEmbedded toEmbedded(DeliveryAddressSnapshot snapshot) {
        return new DeliveryAddressSnapshotEmbedded(
                snapshot.getStreetName(),
                snapshot.getStreetNumber(),
                snapshot.getNeighborhood(),
                snapshot.getCity(),
                snapshot.getStateProvince(),
                snapshot.getPostalCode(),
                snapshot.getAdditionalInfo()
        );
    }

    private static DeliveryAddressSnapshot toDomain(DeliveryAddressSnapshotEmbedded embedded) {
        return new DeliveryAddressSnapshot(
                embedded.getStreetName(),
                embedded.getStreetNumber(),
                embedded.getNeighborhood(),
                embedded.getCity(),
                embedded.getStateProvince(),
                embedded.getPostalCode(),
                embedded.getAdditionalInfo()
        );
    }

    private static OrderItemEmbedded toEmbedded(OrderItem item) {
        return new OrderItemEmbedded(
                item.getMenuItemId(),
                item.getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }

    private static OrderItem toDomain(OrderItemEmbedded embedded) {
        return new OrderItem(
                embedded.getMenuId(),
                embedded.getName(),
                embedded.getQuantity(),
                embedded.getPrice()
        );
    }
}
