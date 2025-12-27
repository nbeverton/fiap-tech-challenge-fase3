package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.usecase.in.menu.*;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuRequest;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuResponse;
import br.com.fiap.techchallenge.infra.persistence.mapper.menu.MenuMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menus")
public class MenuController {

    private final CreateMenuUseCase createMenuUseCase;
    private final UpdateMenuUseCase updateMenuUseCase;
    private final DeleteMenuUseCase deleteMenuUseCase;
    private final FindMenuByIdUseCase findMenuByIdUseCase;
    private final ListMenusByRestaurantUseCase listMenusByRestaurantUseCase;

    public MenuController(
            CreateMenuUseCase createMenuUseCase,
            UpdateMenuUseCase updateMenuUseCase,
            DeleteMenuUseCase deleteMenuUseCase,
            FindMenuByIdUseCase findMenuByIdUseCase,
            ListMenusByRestaurantUseCase listMenusByRestaurantUseCase
    ) {
        this.createMenuUseCase = createMenuUseCase;
        this.updateMenuUseCase = updateMenuUseCase;
        this.deleteMenuUseCase = deleteMenuUseCase;
        this.findMenuByIdUseCase = findMenuByIdUseCase;
        this.listMenusByRestaurantUseCase = listMenusByRestaurantUseCase;
    }

    @PostMapping
    public ResponseEntity<MenuResponse> create(
            @PathVariable String restaurantId,
            @RequestBody MenuRequest request
    ) {
        Menu menu = MenuMapper.toDomain(request);
        Menu created = createMenuUseCase.execute(restaurantId, menu);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MenuMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> list(
            @PathVariable String restaurantId
    ) {
        return ResponseEntity.ok(
                listMenusByRestaurantUseCase.execute(restaurantId)
                        .stream()
                        .map(MenuMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> findById(
            @PathVariable String restaurantId,
            @PathVariable String menuId
    ) {
        return ResponseEntity.ok(
                MenuMapper.toResponse(
                        findMenuByIdUseCase.execute(restaurantId, menuId)
                )
        );
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<MenuResponse> update(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @RequestBody MenuRequest request
    ) {
        Menu menu = MenuMapper.toDomain(request);
        Menu updated = updateMenuUseCase.execute(restaurantId, menuId, menu);

        return ResponseEntity.ok(MenuMapper.toResponse(updated));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> delete(
            @PathVariable String restaurantId,
            @PathVariable String menuId
    ) {
        deleteMenuUseCase.execute(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }
}
