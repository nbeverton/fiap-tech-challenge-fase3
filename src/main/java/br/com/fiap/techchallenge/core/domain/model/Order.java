package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;

import java.math.BigDecimal;
import java.time.Instant;

public class Order {

    private String id;

    private String restaurantId;
    private String userId;
    private String courierId;

    private String deliveryAddress;
    private String description;

    private OrderStatus orderStatus;

    private BigDecimal totalAmount;
    private BigDecimal orderTaxes;

    private Instant createdAt;
    private Instant updatedAt;

    public Order(
            String id,
            String restaurantId,
            String userId,
            String courierId,
            String deliveryAddress,
            String description,
            OrderStatus orderStatus,
            BigDecimal totalAmount,
            BigDecimal orderTaxes,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.courierId = courierId;
        this.deliveryAddress = deliveryAddress;
        this.description = description;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.orderTaxes = orderTaxes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ============================
    // Regras de negócio
    // ============================

    public void accept() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(
                    "Order can only be accepted when status is PENDING."
            );
        }
        this.orderStatus = OrderStatus.ACCEPTED;
        touch();
    }

    public void reject() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException(
                    "Order can only be rejected when status is PENDING."
            );
        }
        this.orderStatus = OrderStatus.REJECTED;
        touch();
    }

    public void startPreparing() {
        if (this.orderStatus != OrderStatus.ACCEPTED) {
            throw new InvalidOrderStatusException(
                    "Order can only start preparing when status is ACCEPTED."
            );
        }
        this.orderStatus = OrderStatus.PREPARING;
        touch();
    }

    public void outForDelivery() {
        if (this.orderStatus != OrderStatus.PREPARING) {
            throw new InvalidOrderStatusException(
                    "Order can only go out for delivery when status is PREPARING."
            );
        }
        this.orderStatus = OrderStatus.OUT_FOR_DELIVERY;
        touch();
    }

    public void deliver() {
        if (this.orderStatus != OrderStatus.OUT_FOR_DELIVERY) {
            throw new InvalidOrderStatusException(
                    "Order can only be delivered when status is OUT_FOR_DELIVERY."
            );
        }
        this.orderStatus = OrderStatus.DELIVERED;
        touch();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // ============================
    // Getters
    // ============================

    public String getId() {
        return id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourierId() {
        return courierId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDescription() {
        return description;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getOrderTaxes() {
        return orderTaxes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
