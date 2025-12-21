package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.domain.model.OpeningHours;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.out.MenuRepositoryPort;
import br.com.fiap.techchallenge.infra.persistence.entity.MenuEntity;
import br.com.fiap.techchallenge.infra.persistence.documents.RestaurantDocument;
import br.com.fiap.techchallenge.infra.persistence.repository.SpringRestaurantRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final SpringRestaurantRepository restaurantRepository;

    public MenuRepositoryAdapter(SpringRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Menu save(String restaurantId, Menu menu) {
        RestaurantDocument restaurantEntity = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> menuList = restaurantEntity.getMenu();
        if (menuList == null) {
            menuList = new ArrayList<>();
            restaurantEntity.setMenu(menuList);
        }

        MenuEntity menuEntity = toEntity(menu);

        int index = indexOfMenu(menuList, menuEntity.getId());
        if (index >= 0) {
            menuList.set(index, menuEntity);
        } else {
            menuList.add(menuEntity);
        }

        RestaurantDocument savedRestaurant = restaurantRepository.save(restaurantEntity);

        MenuEntity savedMenuEntity = savedRestaurant.getMenu().stream()
                .filter(m -> m.getId().equals(menuEntity.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Menu não foi salvo")); // TODO: tratar erro de forma adequada

        return toDomain(savedMenuEntity);
    }

    @Override
    public void deleteById(String restaurantId, String menuId) {
        RestaurantDocument restaurantEntity = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> menus = Optional.ofNullable(restaurantEntity.getMenu())
                .orElseGet(ArrayList::new);
        restaurantEntity.setMenu(menus);

        boolean removed = menus.removeIf(m -> m.getId().equals(menuId));
        if (!removed) {
            throw new IllegalStateException(
                    "Menu " + menuId + " não encontrado para o restaurante " + restaurantId
            );
        }

        restaurantRepository.save(restaurantEntity);
    }

    @Override
    public Optional<Menu> findById(String restaurantId, String menuId) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(rest -> {
                    List<MenuEntity> menus = rest.getMenu();
                    if (menus == null) return Optional.empty();

                    return menus.stream()
                            .filter(m -> m.getId().equals(menuId))
                            .findFirst()
                            .map(this::toDomain);
                });
    }

    @Override
    public List<Menu> findByRestaurantId(String restaurantId) {
        RestaurantDocument restaurantEntity = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> entities = Optional.ofNullable(restaurantEntity.getMenu())
                .orElse(List.of());

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ---------- helpers privados ----------

    private RestaurantDocument getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurante " + restaurantId + " não encontrado"));
    }

    private int indexOfMenu(List<MenuEntity> menus, String menuId) {
        for (int i = 0; i < menus.size(); i++) {
            if (menus.get(i).getId().equals(menuId)) {
                return i;
            }
        }
        return -1;
    }

    private MenuEntity toEntity(Menu menu) {
        MenuEntity entity = new MenuEntity();
        entity.setId(menu.getId());
        entity.setName(menu.getName());
        entity.setDescription(menu.getDescription());
        entity.setPrice(menu.getPrice().doubleValue());
        entity.setDineInAvailable(menu.isDineInAvailable());
        entity.setImageUrl(menu.getImageUrl());
        return entity;
    }

    private Menu toDomain(MenuEntity entity) {
        return  Menu.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                BigDecimal.valueOf(entity.getPrice()),
                entity.isDineInAvailable(),
                entity.getImageUrl()
        );
    }

    private Restaurant toRestaurantDomain(RestaurantDocument entity) {
        List<Menu> menu = Optional.ofNullable(entity.getMenu())
                .orElse(List.of()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());

        OpeningHours opening = null;
        if (entity.getOpeningHours() != null) {
            opening = new OpeningHours(entity.getOpeningHours().getOpens(), entity.getOpeningHours().getCloses());
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
                menu
        );
    }
}
