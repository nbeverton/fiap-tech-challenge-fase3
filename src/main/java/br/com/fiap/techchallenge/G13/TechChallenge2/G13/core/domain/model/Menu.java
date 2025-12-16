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

    public Menu(String id, String name, String description, double price, boolean dineInAvailable, String imageUrl) {

        this.id = requireNonBlank(id, "id");
        this.name = requireNonBlank(name, "name" );
        this.description = description;

        if (price < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        this.price = price;

        this.dineInAvailable = dineInAvailable;
        this.imageUrl = imageUrl;
    }

    public static Menu create(String name,
                              String description,
                              double price,
                              boolean onlyForDineIn,
                              String imageUrl) {
        return new Menu(UUID.randomUUID().toString(),
                name,
                description,
                price,
                onlyForDineIn,
                imageUrl);
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", dineInAvailable=" + dineInAvailable +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
