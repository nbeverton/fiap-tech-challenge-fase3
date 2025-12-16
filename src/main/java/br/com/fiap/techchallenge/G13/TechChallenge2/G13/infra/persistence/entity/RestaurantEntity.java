package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurants")
public class RestaurantEntity {
    @Id
    private String id;

    private String name;
    private String addressId;
    private String cuisineType;
    private OpeningHoursEntity openingHours;
    private String userId;

    private List<MenuEntity> menu;

    public RestaurantEntity() {}

    public RestaurantEntity(String id, String name, String addressId, String cuisineType,
                            OpeningHoursEntity openingHours, String userId, List<MenuEntity> menu) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.cuisineType = cuisineType;
        this.openingHours = openingHours;
        this.userId = userId;
        this.menu = menu;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public OpeningHoursEntity getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursEntity openingHours) {
        this.openingHours = openingHours;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MenuEntity> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuEntity> menu) {
        this.menu = menu;
    }

}