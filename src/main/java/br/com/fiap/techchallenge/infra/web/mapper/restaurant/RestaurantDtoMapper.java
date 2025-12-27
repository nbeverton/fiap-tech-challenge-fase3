package br.com.fiap.techchallenge.infra.web.mapper.restaurant;

import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuResponse;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;
import br.com.fiap.techchallenge.infra.web.mapper.menu.MenuResponseMapper;

import java.util.List;

public class RestaurantDtoMapper {

    private RestaurantDtoMapper() {}

    public static RestaurantResponse toResponse(Restaurant restaurant) {
        if (restaurant == null) return null;

        List<MenuResponse> menu = restaurant.getMenu() == null
                ? List.of()
                : restaurant.getMenu().stream()
                .map(MenuResponseMapper::toResponse)
                .toList();

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
                menu
        );
    }
}
