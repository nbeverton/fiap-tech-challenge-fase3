package br.com.fiap.techchallenge.infra.persistence.adapter;

import br.com.fiap.techchallenge.core.domain.model.Menu;
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
import java.util.UUID;
import java.util.Objects;


@Component
public class MenuRepositoryAdapter implements MenuRepositoryPort {

    private final SpringRestaurantRepository restaurantRepository;

    public MenuRepositoryAdapter(SpringRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Menu save(String restaurantId, Menu menu) {
        RestaurantDocument restaurant = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> menuList = Optional.ofNullable(restaurant.getMenu())
                .orElse(new ArrayList<>());

        MenuEntity menuEntity = toEntity(menu);

        // ✅ Se veio sem id (CREATE), gera um
        if (menuEntity.getId() == null || menuEntity.getId().isBlank()) {
            menuEntity.setId(UUID.randomUUID().toString());
        }

        // Atualiza se já existe
        int index = indexOfMenu(menuList, menuEntity.getId());
        if (index >= 0) {
            menuList.set(index, menuEntity);
        } else {
            menuList.add(menuEntity);
        }

        restaurant.setMenu(menuList);
        RestaurantDocument savedRestaurant = restaurantRepository.save(restaurant);

        MenuEntity savedMenuEntity = savedRestaurant.getMenu().stream()
                .filter(m -> Objects.equals(m.getId(), menuEntity.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Menu não foi salvo"));

        return toDomain(savedMenuEntity);
    }

    @Override
    public void deleteById(String restaurantId, String menuId) {
        RestaurantDocument restaurant = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> menus = Optional.ofNullable(restaurant.getMenu())
                .orElse(new ArrayList<>());

        boolean removed = menus.removeIf(m -> Objects.equals(m.getId(), menuId));
        if (!removed) {
            throw new IllegalStateException("Menu " + menuId + " não encontrado para o restaurante " + restaurantId);
        }

        restaurant.setMenu(menus);
        restaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Menu> findById(String restaurantId, String menuId) {
        return restaurantRepository.findById(restaurantId)
                .flatMap(restaurant -> {
                    List<MenuEntity> menus = restaurant.getMenu();
                    if (menus == null) return Optional.empty();

                    return menus.stream()
                            .filter(m -> Objects.equals(m.getId(), menuId))
                            .findFirst()
                            .map(this::toDomain);
                });
    }

    @Override
    public List<Menu> findByRestaurantId(String restaurantId) {
        RestaurantDocument restaurant = getRestaurantOrThrow(restaurantId);

        List<MenuEntity> menus = Optional.ofNullable(restaurant.getMenu())
                .orElse(List.of());

        return menus.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ---------- Helpers ----------

    private RestaurantDocument getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurante " + restaurantId + " não encontrado"));
    }

    private int indexOfMenu(List<MenuEntity> menus, String menuId) {
        if (menuId == null) return -1;

        for (int i = 0; i < menus.size(); i++) {
            if (Objects.equals(menus.get(i).getId(), menuId)) {
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
        return Menu.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                BigDecimal.valueOf(entity.getPrice()),
                entity.isDineInAvailable(),
                entity.getImageUrl()
        );
    }
}

