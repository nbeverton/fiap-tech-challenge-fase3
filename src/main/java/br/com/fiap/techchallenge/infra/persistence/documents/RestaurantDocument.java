package br.com.fiap.techchallenge.infra.persistence.documents;

import br.com.fiap.techchallenge.infra.persistence.entity.MenuEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "restaurants") // garante que é a collection correta
public class RestaurantDocument {

    @Id
    private String id;

    private String name;

    private String addressId;

    private String cuisineType;

    private OpeningHoursDocument openingHours;

    private String userId;

    @Field("menu") // mapeia a lista de menus no MongoDB
    private List<MenuEntity> menu;

    private String _class; // opcional, usado pelo Spring Data

    public RestaurantDocument() {}

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddressId() { return addressId; }
    public void setAddressId(String addressId) { this.addressId = addressId; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public OpeningHoursDocument getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHoursDocument openingHours) { this.openingHours = openingHours; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<MenuEntity> getMenu() { return menu; }
    public void setMenu(List<MenuEntity> menu) { this.menu = menu; }

    public String get_class() { return _class; }
    public void set_class(String _class) { this._class = _class; }
}
