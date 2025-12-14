package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.adapter;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.MenuRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.MenuEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository.SpringRestaurantRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final SpringRestaurantRepository restaurantRepository;

    public MenuRepositoryAdapter(SpringRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Menu save(String restaurantId, Menu menu) {
        RestaurantEntity restaurantEntity = getRestaurantOrThrow(restaurantId);

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

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurantEntity);

        MenuEntity savedMenuEntity = savedRestaurant.getMenu().stream()
                .filter(m -> m.getId().equals(menuEntity.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Menu não foi salvo")); // TODO: tratar erro de forma adequada

        Restaurant restaurantDomain = toRestaurantDomain(savedRestaurant);

        return toDomain(savedMenuEntity, restaurantDomain);
    }

    @Override
    public void deleteById(String restaurantId, String menuId) {
        RestaurantEntity restaurantEntity = getRestaurantOrThrow(restaurantId);

        // Se não tiver menus, não lança exception customizada por enquanto
        List<MenuEntity> menus = Optional.ofNullable(restaurantEntity.getMenu())
                .orElseGet(ArrayList::new); // TODO: decidir regra de negócio para restaurante sem menus
        restaurantEntity.setMenu(menus);

        boolean removed = menus.removeIf(m -> m.getId().equals(menuId));
        if (!removed) {
            // TODO: substituir por NotFoundException ou outra exception de domínio
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

                    Restaurant restaurantDomain = toRestaurantDomain(rest);

                    return menus.stream()
                            .filter(m -> m.getId().equals(menuId))
                            .findFirst()
                            .map(m -> toDomain(m, restaurantDomain));
                });
    }

    @Override
    public List<Menu> findByRestaurantId(String restaurantId) {
        RestaurantEntity restaurantEntity = getRestaurantOrThrow(restaurantId);
        Restaurant restaurantDomain = toRestaurantDomain(restaurantEntity);

        List<MenuEntity> entities = Optional.ofNullable(restaurantEntity.getMenu())
                .orElse(List.of());

        List<Menu> result = new ArrayList<>();
        for (MenuEntity entity : entities) {
            result.add(toDomain(entity, restaurantDomain));
        }
        return result;
    }

    // ---------- helpers privados ----------

    private RestaurantEntity getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                // TODO: substituir RuntimeException por NotFoundException de domínio
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
        entity.setPrice(menu.getPrice());
        entity.setDineInAvailable(menu.isDineInAvailable());
        entity.setImageUrl(menu.getImageUrl());
        entity.setCreatedAt(menu.getCreatedAt());
        entity.setUpdatedAt(menu.getUpdatedAt());
        return entity;
    }

    private Menu toDomain(MenuEntity entity, Restaurant restaurant) {
        return new Menu(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isDineInAvailable(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                restaurant
        );
    }

    private Restaurant toRestaurantDomain(RestaurantEntity entity) {
        Instant createdAt = entity.getCreatedAt();
        Instant updatedAt = entity.getUpdatedAt();

        if (createdAt == null) {
            createdAt = Instant.now(); // TODO: decidir default melhor
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }

        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType(),
                entity.getOpeningHours(),
                entity.getOwnerId(),
                createdAt,
                updatedAt
        );
    }
}
