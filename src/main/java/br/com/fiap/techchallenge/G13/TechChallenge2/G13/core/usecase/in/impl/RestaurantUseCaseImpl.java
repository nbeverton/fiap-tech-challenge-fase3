package br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.impl;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.in.RestaurantUseCase;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.RestaurantRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantUseCaseImpl implements RestaurantUseCase {

    private final RestaurantRepositoryPort restaurantRepositoryPort;

    public RestaurantUseCaseImpl(RestaurantRepositoryPort restaurantRepositoryPort) {
        this.restaurantRepositoryPort = restaurantRepositoryPort;
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        return restaurantRepositoryPort.save(restaurant);
    }

    @Override
    public Restaurant update(String id, Restaurant restaurant) {
        // TODO: implementar regra de update correta (garantir id, etc.)
        return restaurantRepositoryPort.save(restaurant);
    }

    @Override
    public void delete(String id) {
        restaurantRepositoryPort.delete(id);
    }

    @Override
    public Restaurant findById(String id) {
        // TODO: trocar RuntimeException por exception de domínio (NotFoundException)
        return restaurantRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepositoryPort.findAll();
    }
}
