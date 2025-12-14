package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity;

import java.time.Instant;

public class MenuEntity {

    private String id;
    private String name;
    private String description;
    private double price;
    private boolean dineInAvailable;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public MenuEntity() {
    }

    public MenuEntity(String id,
                      String name,
                      String description,
                      double price,
                      boolean dineInAvailable,
                      String imageUrl,
                      Instant createdAt,
                      Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dineInAvailable = dineInAvailable;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isDineInAvailable() {
        return dineInAvailable;
    }

    public void setDineInAvailable(boolean dineInAvailable) {
        this.dineInAvailable = dineInAvailable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
