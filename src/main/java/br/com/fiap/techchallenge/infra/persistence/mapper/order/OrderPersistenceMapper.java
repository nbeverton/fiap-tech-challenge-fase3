package br.com.fiap.techchallenge.infra.persistence.mapper.order;

import br.com.fiap.techchallenge.core.domain.model.Order;
import br.com.fiap.techchallenge.infra.persistence.documents.OrderDocument;

public class OrderPersistenceMapper {

    public static OrderDocument toDocument(Order order) {
        OrderDocument doc = new OrderDocument();
        doc.setId(order.getId());
        doc.setRestaurantId(order.getRestaurantId());
        doc.setUserId(order.getUserId());

        doc.setUserAddressId(order.getUserAddressId());

        doc.setDeliveryAddress(order.getDeliveryAddress());

        doc.setItems(order.getItems());

        doc.setOrderStatus(order.getOrderStatus());
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
                doc.getDeliveryAddress(),
                doc.getItems(),
                doc.getTotalAmount(),
                doc.getOrderStatus(),
                doc.getCreatedAt(),
                doc.getUpdatedAt()
        );
    }
}
