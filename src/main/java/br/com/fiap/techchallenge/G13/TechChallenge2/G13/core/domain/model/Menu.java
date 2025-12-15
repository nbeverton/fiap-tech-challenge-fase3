package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Menu {
    private final String id;
    private final String name;
    private final String description;
    private final double price;
    private final boolean dineInAvailable;
    private final String imageUrl;

    private final Restaurant restaurant;

    public Menu(String id, String name, String description, double price, boolean dineInAvailable, String imageUrl, Restaurant restaurant) {

        this.id = requireNonBlank(id, "id");
        this.name = requireNonBlank(name, "name" );
        this.description = description;

        if (price < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        this.price = price;

        this.dineInAvailable = dineInAvailable;
        this.imageUrl = imageUrl;
        this.restaurant = Objects.requireNonNull(restaurant, "restaurant must not be null");
    }

    public static Menu create(String name,
                              String description,
                              double price,
                              boolean dineInAvailable,
                              String imageUrl,
                              Restaurant restaurant) {
        return new Menu(UUID.randomUUID().toString(),
                name,
                description,
                price,
                dineInAvailable,
                imageUrl,
                restaurant);
    }

    private static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be null or blank");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isDineInAvailable() {
        return dineInAvailable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", dineInAvailable=" + dineInAvailable +
                ", imageUrl='" + imageUrl + '\'' +
                ", restaurant=" + restaurant +
                '}';
    }
}
