package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.controller;


import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Menu;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.MenuUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.MenuDto;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper.MenuMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menus")
public class MenuController {

    private final MenuUseCase menuUseCase;
    private final RestaurantUseCase restaurantUseCase;

    public MenuController(MenuUseCase menuUseCase,
                          RestaurantUseCase restaurantUseCase) {
        this.menuUseCase = menuUseCase;
        this.restaurantUseCase = restaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<Menu> create(@PathVariable String restaurantId,
                                       @RequestBody MenuDto dto) {

        Restaurant restaurant = restaurantUseCase.findById(restaurantId);
        Menu menu = MenuMapper.toDomain(dto, restaurant);
        Menu created = menuUseCase.create(restaurantId, menu);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Menu>> listByRestaurant(@PathVariable String restaurantId) {
        List<Menu> menus = menuUseCase.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(menus);
    }

}
