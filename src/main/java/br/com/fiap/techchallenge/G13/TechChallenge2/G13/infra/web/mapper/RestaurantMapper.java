package br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.mapper;

import br.com.fiap.techchallenge.G13.TechChallenge2.G13.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.persistence.entity.RestaurantEntity;
import br.com.fiap.techchallenge.G13.TechChallenge2.G13.infra.web.dto.RestaurantDto;

public class RestaurantMapper {

    public static Restaurant toDomain(RestaurantDto dto) {
        Restaurant r = new Restaurant();
        r.setName(dto.name());
        r.setAddress(dto.address());
        r.setCuisineType(dto.cuisineType());
        r.setOpeningHours(dto.openingHours());
        r.setOwnerId(dto.ownerId());
        return r;
    }

    public static RestaurantEntity toEntity(Restaurant domain) {
        RestaurantEntity e = new RestaurantEntity();
        e.setId(domain.getId());
        e.setName(domain.getName());
        e.setAddress(domain.getAddress());
        e.setCuisineType(domain.getCuisineType());
        e.setOpeningHours(domain.getOpeningHours());
        e.setOwnerId(domain.getOwnerId());
        return e;
    }
}
