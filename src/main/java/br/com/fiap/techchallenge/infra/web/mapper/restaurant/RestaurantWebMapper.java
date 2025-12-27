package br.com.fiap.techchallenge.infra.web.mapper.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Menu;
import br.com.fiap.techchallenge.core.domain.model.OpeningHours;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantRequest;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;
import br.com.fiap.techchallenge.infra.web.mapper.menu.MenuWebMapper;

import java.util.List;

public class RestaurantWebMapper {

    private RestaurantWebMapper() {}

    public static Restaurant toDomain(RestaurantRequest request) {

        OpeningHours openingHours = request.openingHours() == null
                ? null
                : new OpeningHours(
                request.openingHours().opens(),
                request.openingHours().closes()
        );

        List<Menu> menu = request.menu() == null
                ? List.of()
                : MenuWebMapper.toDomainList(request.menu());

        return Restaurant.create(
                request.name(),
                request.addressId(),
                CuisineType.valueOf(request.cuisineType()),
                openingHours,
                request.userId(),
                menu
        );
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddressId(),
                restaurant.getCuisineType().name(),
                restaurant.getOpeningHours() == null
                        ? null
                        : new RestaurantResponse.OpeningHoursResponse(
                        restaurant.getOpeningHours().getOpens(),
                        restaurant.getOpeningHours().getCloses()
                ),
                restaurant.getUserId(),
                MenuWebMapper.toResponseList(restaurant.getMenu())
        );
    }
}
