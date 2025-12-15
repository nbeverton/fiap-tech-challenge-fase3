package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Restaurant {

    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private String ownerId;

    public Restaurant(String id,
                      String name,
                      String address,
                      String cuisineType,
                      String openingHours,
                      String ownerId
                      ) {

        this.id = requireNonBlank(id, "id");
        this.name = requireNonBlank(name, "name");
        this.address = requireNonBlank(address, "address");
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.ownerId = requireNonBlank(ownerId, "ownerId");
    }

    public static Restaurant create(String name,
                                    String address,
                                    String cuisineType,
                                    String openingHours,
                                    String ownerId) {
        return new Restaurant(
                UUID.randomUUID().toString(),
                name,
                address,
                cuisineType,
                openingHours,
                ownerId
        );
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

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getOwnerId() {
        return ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", cuisineType='" + cuisineType + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}
