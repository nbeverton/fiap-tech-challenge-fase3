package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.adapter;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository.SpringRestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final SpringRestaurantRepository repo;

    public RestaurantRepositoryAdapter(SpringRestaurantRepository repo) {
        this.repo = repo;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = toEntity(restaurant);
        RestaurantEntity saved = repo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(String id) {
        return repo.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repo.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }

    private RestaurantEntity toEntity(Restaurant restaurant) {
        RestaurantEntity entity = new RestaurantEntity();

        entity.setId(restaurant.getId());
        entity.setName(restaurant.getName());
        entity.setAddress(restaurant.getAddress());
        entity.setCuisineType(restaurant.getCuisineType());
        entity.setOpeningHours(restaurant.getOpeningHours());
        entity.setOwnerId(restaurant.getOwnerId());

        return entity;
    }

    private Restaurant toDomain(RestaurantEntity entity) {
        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType(),
                entity.getOpeningHours(),
                entity.getOwnerId()
        );
    }
}
