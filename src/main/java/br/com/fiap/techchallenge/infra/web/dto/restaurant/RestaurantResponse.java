package br.com.fiap.techchallenge.infra.web.dto.restaurant;

import java.util.List;
import br.com.fiap.techchallenge.infra.web.dto.menu.MenuResponse;

public record RestaurantResponse(
        String id,
        String name,
        String addressId,
        String cuisineType,
        OpeningHoursResponse openingHours,
        String userId,
        List<MenuResponse> menu
) {

    public record OpeningHoursResponse(
            String opens,
            String closes
    ) {}

}
