package br.com.fiap.techchallenge.infra.web.controller;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.infra.web.dto.RestaurantDto;
import br.com.fiap.techchallenge.infra.web.mapper.RestaurantMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantUseCase service;

    public RestaurantController(RestaurantUseCase service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody RestaurantDto dto) {
        Restaurant restaurant = RestaurantMapper.toDomain(dto);
        Restaurant created = service.create(restaurant);
        RestaurantDto response = RestaurantMapper.toDto(created);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll() {
        List<Restaurant> list = service.findAll();
        List<RestaurantDto> dtos = list.stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> findById(@PathVariable String id) {
        Restaurant r = service.findById(id);
        return ResponseEntity.ok(RestaurantMapper.toDto(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable String id, @RequestBody RestaurantDto dto) {
        Restaurant restaurant = RestaurantMapper.toDomain(dto);
        Restaurant updated = service.update(id, restaurant);
        return ResponseEntity.ok(RestaurantMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
