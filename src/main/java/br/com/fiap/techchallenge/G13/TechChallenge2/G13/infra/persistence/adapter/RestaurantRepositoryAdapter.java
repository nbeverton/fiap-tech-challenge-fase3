package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.adapter;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.usecase.out.RestaurantRepositoryPort;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.repository.SpringRestaurantRepository;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper.RestaurantMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantRepositoryAdapter implements RestaurantRepositoryPort {

    private final SpringRestaurantRepository repo;

    public RestaurantRepositoryAdapter(SpringRestaurantRepository repo) {
        this.repo = repo;
    }

    @Override
    public Restaurant save(Restaurant r) {
        RestaurantEntity entity = RestaurantMapper.toEntity(r);
        return RestaurantMapper.toDomain(repo.save(entity));
    }
}
