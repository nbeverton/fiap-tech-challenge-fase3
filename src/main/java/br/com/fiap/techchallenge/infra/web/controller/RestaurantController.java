package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.restaurant.RestaurantUseCase;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantRequest;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;
import br.com.fiap.techchallenge.infra.web.mapper.restaurant.RestaurantMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantUseCase service;

    public RestaurantController(RestaurantUseCase service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @RequestBody RestaurantRequest request) {

        Restaurant restaurant = RestaurantMapper.toDomain(request);
        Restaurant created = service.create(restaurant);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestaurantMapper.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(
                service.findAll()
                        .stream()
                        .map(RestaurantMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(
                RestaurantMapper.toResponse(service.findById(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable String id,
            @RequestBody RestaurantRequest request) {

        Restaurant restaurant = RestaurantMapper.toDomain(request);
        Restaurant updated = service.update(id, restaurant);

        return ResponseEntity.ok(RestaurantMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
