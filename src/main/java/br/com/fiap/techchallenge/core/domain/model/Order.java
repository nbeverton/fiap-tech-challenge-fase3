package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.core.domain.exception.order.InvalidOrderStatusException;
import br.com.fiap.techchallenge.core.domain.valueobjects.DeliveryAddressSnapshot;
import br.com.fiap.techchallenge.core.domain.valueobjects.OrderItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final String id;
    private final String restaurantId;
    private final String userId;
    private final String userAddressId;

    private DeliveryAddressSnapshot deliveryAddress;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;

    private final Instant createdAt;
    private Instant updatedAt;

    public Order(String id,
                 String restaurantId,
                 String userId,
                 String userAddressId,
                 DeliveryAddressSnapshot deliveryAddress,
                 List<OrderItem> items,
                 BigDecimal totalAmount,
                 OrderStatus orderStatus,
                 Instant createdAt,
                 Instant updatedAt)
    {
        this.id = id;
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.userAddressId = userAddressId;

        this.deliveryAddress = deliveryAddress;

        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();

        this.totalAmount = (totalAmount != null) ? totalAmount : BigDecimal.ZERO;

        this.orderStatus = (orderStatus != null) ? orderStatus : OrderStatus.CREATED;

        this.createdAt = (createdAt != null) ? createdAt : Instant.now();

        this.updatedAt = (updatedAt != null) ? updatedAt : this.createdAt;
    }


    // ============================
    // Regras de negócio
    // ============================

    /**
     * CREATED -> AWAITING_PAYMENT
     */
    public void awaitPayment() {
        requireStatus(OrderStatus.CREATED, "Order can only await payment when status is CREATED.");
        this.orderStatus = OrderStatus.AWAITING_PAYMENT;
        touch();
    }

    /**
     * AWAITING_PAYMENT -> PAID
     */
    public void markAsPaid() {
        requireStatus(OrderStatus.AWAITING_PAYMENT, "Order can only be marked as PAID when status is AWAITING_PAYMENT.");
        this.orderStatus = OrderStatus.PAID;
        touch();
    }

    /**
     * PAID -> PREPARING
     */
    public void startPreparing() {
        requireStatus(OrderStatus.PAID, "Order can only start preparing when status is PAID.");
        this.orderStatus = OrderStatus.PREPARING;
        touch();
    }

    /**
     * PREPARING -> OUT_FOR_DELIVERY
     */
    public void outForDelivery() {
        requireStatus(OrderStatus.PREPARING, "Order can only go out for delivery when status is PREPARING.");
        this.orderStatus = OrderStatus.OUT_FOR_DELIVERY;
        touch();
    }

    /**
     * OUT_FOR_DELIVERY -> DELIVERED
     */
    public void deliver() {
        requireStatus(OrderStatus.OUT_FOR_DELIVERY, "Order can only be delivered when status is OUT_FOR_DELIVERY.");
        this.orderStatus = OrderStatus.DELIVERED;
        touch();
    }

    /**
     * Cancellation allowed at early stages
     */
    public void cancel() {
        if (this.orderStatus == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Order cannot be canceled when status is DELIVERED.");
        }

        if (this.orderStatus == OrderStatus.CANCELED) {
            return; // idempotente
        }

        this.orderStatus = OrderStatus.CANCELED;
        touch();
    }

    private void requireStatus(OrderStatus expected, String messageIfInvalid) {
        if (this.orderStatus != expected) {
            throw new InvalidOrderStatusException(messageIfInvalid);
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }


    // ============================
    // Getters (útil p/ UseCases, mappers, presenters)
    // ============================

    public String getId() { return id; }
    public String getRestaurantId() { return restaurantId; }
    public String getUserId() { return userId; }
    public String getUserAddressId() { return userAddressId; }

    public DeliveryAddressSnapshot getDeliveryAddress() { return deliveryAddress; }

    // evita alguém alterar a lista diretamente
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getOrderStatus() { return orderStatus; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
