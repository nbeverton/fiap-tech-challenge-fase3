package br.com.fiap.techchallenge.core.domain.valueobjects;

import java.math.BigDecimal;

public class OrderItem {

    private String menuItemId;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public OrderItem(String menuItemId, String name, Integer quantity, BigDecimal price) {
        this.menuItemId = menuItemId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}