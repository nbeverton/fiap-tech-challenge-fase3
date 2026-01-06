package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.exception.menu.InvalidMenuException;

import java.math.BigDecimal;

public class Menu {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean dineInAvailable;
    private String imageUrl;

    private Menu(String id,
                 String name,
                 String description,
                 BigDecimal price,
                 boolean dineInAvailable,
                 String imageUrl) {

        this.id = id;
        this.name = requireNonBlank(name, "name");
        this.description = description;
        this.price = requireNonNegative(price, "price");
        this.dineInAvailable = dineInAvailable;
        this.imageUrl = imageUrl;
    }

    public static Menu create(String name,
                              String description,
                              BigDecimal price,
                              boolean dineInAvailable,
                              String imageUrl) {
        return new Menu(null, name, description, price, dineInAvailable, imageUrl);
    }

    public static Menu restore(String id,
                               String name,
                               String description,
                               BigDecimal price,
                               boolean dineInAvailable,
                               String imageUrl) {
        return new Menu(id, name, description, price, dineInAvailable, imageUrl);
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidMenuException(fieldName + " must not be null or blank");
        }
        return value;
    }

    private BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new InvalidMenuException(fieldName + " must not be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuException(fieldName + " must not be negative");
        }
        return value;
    }


    // ✅ GETTERS NECESSÁRIOS
    public String getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public boolean isDineInAvailable() {
        return dineInAvailable;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
