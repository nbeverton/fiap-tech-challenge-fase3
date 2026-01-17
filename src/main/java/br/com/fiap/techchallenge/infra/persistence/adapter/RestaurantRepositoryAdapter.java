package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.MenuEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.embedded.OpeningHoursEmbedded;
import br.com.fiap.techchallenge.infra.persistence.documents.RestaurantDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringRestaurantRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public boolean existsByUserId(String userId) {
        return repo.existsByUserId(userId);
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantDocument entity = toEntity(restaurant);
        RestaurantDocument saved = repo.save(entity);
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

    @Override
    public Optional<Restaurant> findByAddressId(String addressId) {
        return repo.findByAddressId(addressId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Restaurant> findByName(String name) {
        return repo.findByName(name) // ou findByNameIgnoreCase(name)
                .map(this::toDomain);
    }

    // ---------- Conversão Domain -> Document ----------
    private RestaurantDocument toEntity(Restaurant restaurant) {
        RestaurantDocument entity = new RestaurantDocument();

        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddressId(restaurant.getAddressId());
        entity.setCuisineType(restaurant.getCuisineType() == null ? null : restaurant.getCuisineType().name());

        OpeningHours oh = restaurant.getOpeningHours();
        if (oh != null) {
            // Convertendo OpeningHoursEntity para OpeningHoursDocument
            entity.setOpeningHours(toDocument(new OpeningHoursEmbedded(oh.getOpens(), oh.getCloses())));
        }

        entity.setUserId(restaurant.getUserId());

        List<MenuEmbedded> menuEntities = restaurant.getMenu().stream()
                .map(this::menuToEntity)
                .collect(Collectors.toList());

        entity.setMenu(menuEntities);

        return entity;
    }

    // ---------- Conversão Document -> Domain ----------
    private Restaurant toDomain(RestaurantDocument entity) {
        List<Menu> menu = entity.getMenu() == null ? List.of()
                : entity.getMenu().stream()
                        .map(this::menuToDomain)
                        .collect(Collectors.toList());

        OpeningHours opening = null;
        if (entity.getOpeningHours() != null) {
            opening = new OpeningHours(
                    entity.getOpeningHours().getOpens(),
                    entity.getOpeningHours().getCloses());
        }

        CuisineType cuisine;
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
                menu);
    }

    // ---------- Conversão Menu ----------
    private MenuEmbedded menuToEntity(Menu m) {
        MenuEmbedded me = new MenuEmbedded();
        me.setId(m.getId());
        me.setName(m.getName());
        me.setDescription(m.getDescription());
        me.setPrice(m.getPrice().doubleValue());
        me.setDineInAvailable(m.isDineInAvailable());
        me.setImageUrl(m.getImageUrl());
        return me;
    }

    private Menu menuToDomain(MenuEmbedded me) {
        return Menu.restore(
                me.getId(),
                me.getName(),
                me.getDescription(),
                BigDecimal.valueOf(me.getPrice()),
                me.isDineInAvailable(),
                me.getImageUrl());
    }

    // ---------- Conversão OpeningHours ----------
    private OpeningHoursEmbedded toDocument(OpeningHoursEmbedded entity) {
        if (entity == null)
            return null;
        return new OpeningHoursEmbedded(entity.getOpens(), entity.getCloses());
    }
}
