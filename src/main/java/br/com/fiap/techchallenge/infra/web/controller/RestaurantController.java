package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.CreateRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.DeleteRestaurantUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.ListRestaurantsUseCase;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantRequest;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;
import br.com.fiap.techchallenge.infra.persistence.mapper.restaurant.RestaurantMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            FindRestaurantByIdUseCase findRestaurantByIdUseCase,
            ListRestaurantsUseCase listRestaurantsUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.listRestaurantsUseCase = listRestaurantsUseCase;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @RequestBody RestaurantRequest request) {
        Restaurant restaurant = RestaurantMapper.toDomain(request);
        Restaurant created = createRestaurantUseCase.execute(restaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestaurantMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        List<RestaurantResponse> response = listRestaurantsUseCase.execute()
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(
            @PathVariable String id) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(id);
        return ResponseEntity.ok(RestaurantMapper.toResponse(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable String id,
            @RequestBody RestaurantRequest request) {
        Restaurant restaurant = RestaurantMapper.toDomain(request);
        Restaurant updated = updateRestaurantUseCase.execute(id, restaurant);

        return ResponseEntity.ok(RestaurantMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    // ---------------- Menu to Restaurant

    @PostMapping("/{restaurantId}/menus")
    public ResponseEntity<RestaurantResponse> addMenu(
            @PathVariable String restaurantId,
            @RequestBody Menu menuRequest) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);

        Menu menu = Menu.restore(
                UUID.randomUUID().toString(),
                menuRequest.getName(),
                menuRequest.getDescription(),
                menuRequest.getPrice(),
                menuRequest.isDineInAvailable(),
                menuRequest.getImageUrl());

        Restaurant saved = updateRestaurantUseCase.execute(restaurantId, restaurant.addMenu(menu));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestaurantMapper.toResponse(saved));
    }

    @GetMapping("/{restaurantId}/menus")
    public ResponseEntity<List<Menu>> getMenus(
            @PathVariable String restaurantId) {

        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);
        return ResponseEntity.ok(restaurant.getMenu());
    }

    @PutMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<RestaurantResponse> updateMenu(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @RequestBody Menu menuRequest) {

        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);

        Menu updatedMenu = Menu.restore(
                menuId,
                menuRequest.getName(),
                menuRequest.getDescription(),
                menuRequest.getPrice(),
                menuRequest.isDineInAvailable(),
                menuRequest.getImageUrl());

        Restaurant updated = restaurant.updateMenu(updatedMenu);
        Restaurant saved = updateRestaurantUseCase.execute(restaurantId, updated);

        return ResponseEntity.ok(RestaurantMapper.toResponse(saved));
    }

    @DeleteMapping("/{restaurantId}/menus/{menuId}")
    public ResponseEntity<RestaurantResponse> deleteMenu(
            @PathVariable String restaurantId,
            @PathVariable String menuId) {
        Restaurant restaurant = findRestaurantByIdUseCase.execute(restaurantId);

        Restaurant updated = restaurant.removeMenu(menuId);

        Restaurant saved = updateRestaurantUseCase.execute(restaurantId, updated);

        return ResponseEntity.ok(RestaurantMapper.toResponse(saved));
    }

}
