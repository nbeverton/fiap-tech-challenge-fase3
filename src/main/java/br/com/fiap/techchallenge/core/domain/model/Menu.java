package br.com.fiap.techchallenge.core.domain.model;

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
        this.name = name;
        this.description = description;
        this.price = price;
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
