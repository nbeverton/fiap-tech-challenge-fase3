package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.adapter;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.OpeningHours;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.MenuEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.OpeningHoursEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository.SpringRestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final SpringRestaurantRepository repo;

    public RestaurantRepositoryAdapter(SpringRestaurantRepository repo) {
        this.repo = repo;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = toEntity(restaurant);
        RestaurantEntity saved = repo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(String id) {
        return repo.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repo.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }

    private RestaurantEntity toEntity(Restaurant restaurant) {
        RestaurantEntity entity = new RestaurantEntity();

        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddressId(restaurant.getAddressId());
        entity.setCuisineType(restaurant.getCuisineType() == null ? null : restaurant.getCuisineType().name());
        OpeningHours oh = restaurant.getOpeningHours();
        if (oh != null) {
            entity.setOpeningHours(new OpeningHoursEntity(oh.getOpens(), oh.getCloses()));
        }
        entity.setUserId(restaurant.getUserId());

        List<MenuEntity> menuEntities = restaurant.getMenu().stream()
                .map(this::menuToEntity)
                .collect(Collectors.toList());
        entity.setMenu(menuEntities);

        return entity;
    }

    private Restaurant toDomain(RestaurantEntity entity) {
        List<Menu> menu = entity.getMenu() == null ? List.of() : entity.getMenu().stream()
                .map(this::menuToDomain)
                .collect(Collectors.toList());

        OpeningHours opening = null;
        if (entity.getOpeningHours() != null) {
            opening = new OpeningHours(entity.getOpeningHours().getOpens(), entity.getOpeningHours().getCloses());
        }

        // convert cuisineType string to enum if possible
        CuisineType cuisine = null;
        if (entity.getCuisineType() != null) {
            try {
                cuisine = CuisineType.valueOf(entity.getCuisineType());
            } catch (Exception e) {
                cuisine = CuisineType.OTHER;
            }
        } else {
            cuisine = CuisineType.OTHER;
        }

        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddressId(),
                cuisine,
                opening,
                entity.getUserId(),
                menu
        );
    }

    private MenuEntity menuToEntity(Menu m) {
        MenuEntity me = new MenuEntity();
        me.setId(m.getId());
        me.setName(m.getName());
        me.setDescription(m.getDescription());
        me.setPrice(m.getPrice());
        me.setDineInAvailable(m.isDineInAvailable());
        me.setImageUrl(m.getImageUrl());
        return me;
    }

    private Menu menuToDomain(MenuEntity me) {
        return new Menu(
                me.getId(),
                me.getName(),
                me.getDescription(),
                me.getPrice(),
                me.isDineInAvailable(),
                me.getImageUrl()
        );
    }
}
