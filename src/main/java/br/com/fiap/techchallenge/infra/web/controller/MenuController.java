package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.MenuUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menus")
public class MenuController {

    private final MenuUseCase menuUseCase;

    public MenuController(MenuUseCase menuUseCase) {
        this.menuUseCase = menuUseCase;
    }

    @PostMapping
    public ResponseEntity<Menu> create(
            @PathVariable String restaurantId,
            @RequestBody Menu menu) {

        Menu created = menuUseCase.create(restaurantId, menu);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> listByRestaurant(
            @PathVariable String restaurantId) {

        return ResponseEntity.ok(
                menuUseCase.findByRestaurantId(restaurantId)
        );
    }
}
