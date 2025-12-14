package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.controller;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.RestaurantDto;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper.RestaurantMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantUseCase service;

    public RestaurantController(RestaurantUseCase service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody RestaurantDto dto) {
        Restaurant restaurant = RestaurantMapper.toDomain(dto);
        Restaurant created = service.create(restaurant);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}
