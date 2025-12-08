package br.com.fiap.techchallenge.G13.TechChallenge2.G13.domain.entity;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Cardapio {
    private final UUID id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean onlyForDineIn;
    private final String photoPath;
    private final Restaurant restaurant;

    public Cardapio(UUID id, String name, String description, BigDecimal price, boolean onlyForDineIn, String photoPath, Restaurant restaurant) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null" );
        this.description = description;
        this.price = Objects.requireNonNull(price, "price must not be null");
            if (price.signum() < 0) {
                throw  new IllegalArgumentException("price must not be negative");
            }
        this.onlyForDineIn = onlyForDineIn;
        this.photoPath = photoPath;
        this.restaurant = Objects.requireNonNull(restaurant, "restaurant must not be null");
    }

    public static Cardapio create(String name,
                                  String description,
                                  BigDecimal price,
                                  boolean onlyForDineIn,
                                  String photoPath,
                                  Restaurant restaurant) {
        return new Cardapio(UUID.randomUUID(),
                name,
                description,
                price,
                onlyForDineIn,
                photoPath,
                restaurant);
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new NullPointerException(fieldName + " must not be null or blank");
        }
        return value;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isOnlyForDineIn() {
        return onlyForDineIn;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cardapio cardapio = (Cardapio) o;
        return onlyForDineIn == cardapio.onlyForDineIn && Objects.equals(id, cardapio.id) && Objects.equals(name, cardapio.name) && Objects.equals(description, cardapio.description) && Objects.equals(price, cardapio.price) && Objects.equals(photoPath, cardapio.photoPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, onlyForDineIn, photoPath);
    }

    @Override
    public String toString() {
        return "Cardapio{" +
                "id=" + id +
                ", itemName='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", onlyOnRestaurant=" + onlyForDineIn +
                ", restaurant=" + (restaurant != null ? restaurant.getClass().getSimpleName() : "null") +
                ", dishPhoto='" + photoPath + '\'' +
                '}';
    }
}
