package br.com.fiap.techchallenge.core.domain.model;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Restaurant {

    private final String id;
    private final String name;
    private final String addressId;
    private final CuisineType cuisineType;
    private final OpeningHours openingHours;
    private final String userId;

    private final List<Menu> menu;

    public Restaurant(String id,
                      String name,
                      String addressId,
                      CuisineType cuisineType,
                      OpeningHours openingHours,
                      String userId,
                      List<Menu> menu) {

        this.id = requireNonBlank(id, "id");
        this.name = requireNonBlank(name, "name");
        this.addressId = requireNonBlank(addressId, "addressId");
        this.cuisineType = Objects.requireNonNull(cuisineType, "cuisineType must not be null");
        this.openingHours = Objects.requireNonNull(openingHours, "openingHours must not be null");
        this.userId = requireNonBlank(userId, "userId");
        this.menu = menu == null ? List.of() : List.copyOf(menu);
    }

    public static Restaurant create(String name,
                                    String addressId,
                                    CuisineType cuisineType,
                                    OpeningHours openingHours,
                                    String userId,
                                    List<Menu> menu) {
        return new Restaurant(
                UUID.randomUUID().toString(),
                name,
                addressId,
                cuisineType,
                openingHours,
                userId,
                menu
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

    public String getAddressId() {
        return addressId;
    }

    public CuisineType getCuisineType() {
        return cuisineType;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public String getUserId() {
        return userId;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    // ---------- Domain behaviours to manage menus (immutable style) ----------

    public Restaurant addMenu(Menu newMenu) {
        Objects.requireNonNull(newMenu, "menu must not be null");
        // ensure no duplicate id
        for (Menu m : this.menu) {
            if (m.getId().equals(newMenu.getId())) {
                throw new IllegalArgumentException("Menu with id already exists: " + newMenu.getId());
            }
        }
        List<Menu> newList = new ArrayList<>(this.menu);
        newList.add(newMenu);
        return new Restaurant(this.id, this.name, this.addressId, this.cuisineType, this.openingHours, this.userId, newList);
    }

    public Restaurant updateMenu(Menu updatedMenu) {
        Objects.requireNonNull(updatedMenu, "menu must not be null");
        List<Menu> newList = new ArrayList<>(this.menu);
        boolean found = false;
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).getId().equals(updatedMenu.getId())) {
                newList.set(i, updatedMenu);
                found = true;
                break;
            }
        }
        if (!found) throw new NotFoundException("Menu not found: " + updatedMenu.getId());
        return new Restaurant(this.id, this.name, this.addressId, this.cuisineType, this.openingHours, this.userId, newList);
    }

    public Restaurant removeMenu(String menuId) {
        Objects.requireNonNull(menuId, "menuId must not be null");
        List<Menu> newList = new ArrayList<>(this.menu);
        boolean removed = newList.removeIf(m -> m.getId().equals(menuId));
        if (!removed) throw new NotFoundException("Menu not found: " + menuId);
        return new Restaurant(this.id, this.name, this.addressId, this.cuisineType, this.openingHours, this.userId, newList);
    }

    // -------------------------------------------------------------------------

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", addressId='" + addressId + '\'' +
                ", cuisineType=" + cuisineType +
                ", openingHours=" + openingHours +
                ", userId='" + userId + '\'' +
                ", menu=" + menu +
                '}';
    }
}
