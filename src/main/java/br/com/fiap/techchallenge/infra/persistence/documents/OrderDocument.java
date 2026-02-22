package br.com.fiap.techchallenge.infra.persistence.documents;

import br.com.fiap.techchallenge.core.domain.enums.OrderStatus;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.DeliveryAddressSnapshotEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.OrderItemEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
public class OrderDocument {

    @Id
    private String id;

    private String restaurantId;
    
    private String userId;

    private String userAddressId;

    private DeliveryAddressSnapshotEmbedded deliveryAddress;

    private List<OrderItemEmbedded> items;

    private String orderStatus;

    private BigDecimal totalAmount;

    private Instant createdAt;
    private Instant updatedAt;

    // --------------------
    // getters e setters
    // --------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(String userAddressId) {
        this.userAddressId = userAddressId;
    }

    public DeliveryAddressSnapshotEmbedded getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddressSnapshotEmbedded deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItemEmbedded> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEmbedded> items) {
        this.items = items;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
