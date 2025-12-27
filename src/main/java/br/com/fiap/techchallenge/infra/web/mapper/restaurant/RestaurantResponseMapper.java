package br.com.fiap.techchallenge.infra.web.mapper.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;
import br.com.fiap.techchallenge.infra.web.mapper.menu.MenuResponseMapper;

public class RestaurantResponseMapper {

    private RestaurantResponseMapper() {
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddressId(),
                restaurant.getCuisineType() == null
                        ? null
                        : restaurant.getCuisineType().name(),
                restaurant.getOpeningHours() == null
                        ? null
                        : new RestaurantResponse.OpeningHoursResponse(
                        restaurant.getOpeningHours().getOpens(),
                        restaurant.getOpeningHours().getCloses()
                ),
                restaurant.getUserId(),
                restaurant.getMenu() == null
                        ? null
                        : restaurant.getMenu().stream()
                        .map(MenuResponseMapper::toResponse)
                        .toList()
        );
    }
}
