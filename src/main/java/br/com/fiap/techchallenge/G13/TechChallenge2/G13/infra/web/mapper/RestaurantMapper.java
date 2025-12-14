package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.RestaurantDto;

public class RestaurantMapper {

    public static Restaurant toDomain(RestaurantDto dto) {
        return Restaurant.create(
                dto.name(),
                dto.address(),
                dto.cuisineType(),
                dto.openingHours(),
                dto.ownerId()
        );
    }
}
