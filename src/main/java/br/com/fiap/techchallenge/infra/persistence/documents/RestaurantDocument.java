package br.com.fiap.techchallenge.infra.persistence.documents;

import br.com.fiap.techchallenge.infra.persistence.documents.embedded.MenuEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.OpeningHoursEmbedded;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "restaurants") // ensures the correct MongoDB collection is used
public class RestaurantDocument {

    @Id
    private String id;

    private String name;

    private String addressId;

    private String cuisineType;

    private OpeningHoursEmbedded openingHours;

    private String userId;

    @Field("menu") // maps the menu list in MongoDB
    private List<MenuEmbedded> menu;

    private String _class; // optional, used internally by Spring Data

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

    public OpeningHoursEmbedded getOpeningHours() { return openingHours; }
    public void setOpeningHours(OpeningHoursEmbedded openingHours) { this.openingHours = openingHours; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public List<MenuEmbedded> getMenu() { return menu; }
    public void setMenu(List<MenuEmbedded> menu) { this.menu = menu; }

    public String get_class() { return _class; }
    public void set_class(String _class) { this._class = _class; }
}
