package br.com.fiap.techchallenge.infra.persistence.documents.embedded;

import java.math.BigDecimal;

public class OrderItemEmbedded {

    private String menuItemId;
    private String name;
    private Integer quantity;
    private BigDecimal price;


    public OrderItemEmbedded() {
    }

    public OrderItemEmbedded(String menuId, String name, Integer quantity, BigDecimal price) {
        this.menuItemId = menuId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }


    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
