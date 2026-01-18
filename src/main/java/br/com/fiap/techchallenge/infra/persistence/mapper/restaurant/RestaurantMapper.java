package br.com.fiap.techchallenge.infra.persistence.mapper.restaurant;

import br.com.fiap.techchallenge.core.domain.enums.CuisineType;
import br.com.fiap.techchallenge.core.domain.model.Restaurant;
import br.com.fiap.techchallenge.core.domain.valueobjects.Menu;
import br.com.fiap.techchallenge.core.domain.valueobjects.OpeningHours;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantRequest;
import br.com.fiap.techchallenge.infra.web.dto.restaurant.RestaurantResponse;

import java.util.List;

public class RestaurantMapper {

    private RestaurantMapper() {}

    /* ======================
       REQUEST -> DOMAIN
       ====================== */

    public static Restaurant toDomain(RestaurantRequest request) {
        if (request == null) return null;

        OpeningHours opening = new OpeningHours(
                request.openingHours().opens(),
                request.openingHours().closes()
        );

        List<Menu> menu = request.menu() == null
                ? List.of()
                : request.menu().stream()
                .map(RestaurantMapper::toMenuDomain)
                .toList();

        return Restaurant.create(
                request.name(),
                request.addressId(),
                CuisineType.fromString(request.cuisineType()),
                opening,
                request.userId(),
                menu
        );
    }

    /* ======================
       DOMAIN -> RESPONSE
       ====================== */

    public static RestaurantResponse toResponse(Restaurant domain) {
        if (domain == null) return null;

        List<RestaurantResponse.MenuResponse> menu =
                domain.getMenu().stream()
                        .map(RestaurantMapper::toMenuResponse)
                        .toList();

        return new RestaurantResponse(
                domain.getId(),
                domain.getName(),
                domain.getAddressId(),
                domain.getCuisineType().name(),
                new RestaurantResponse.OpeningHoursResponse(
                        domain.getOpeningHours().getOpens(),
                        domain.getOpeningHours().getCloses()
                ),
                domain.getUserId(),
                menu
        );
    }

    /* ======================
       MENU (INTERNO)
       ====================== */

    private static Menu toMenuDomain(RestaurantRequest.MenuRequest dto) {
        return Menu.create(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.dineInAvailable(),
                dto.imageUrl()
        );
    }

    private static RestaurantResponse.MenuResponse toMenuResponse(Menu menu) {
        return new RestaurantResponse.MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice().doubleValue(),
                menu.isDineInAvailable(),
                menu.getImageUrl()
        );
    }
}
